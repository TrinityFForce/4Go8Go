package org.trinityfforce.sagopalgo.bid.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trinityfforce.sagopalgo.bid.dto.BidItemResponseDto;
import org.trinityfforce.sagopalgo.bid.dto.BidRequestDto;
import org.trinityfforce.sagopalgo.bid.dto.BidUserResponseDto;
import org.trinityfforce.sagopalgo.bid.entity.Bid;
import org.trinityfforce.sagopalgo.bid.repository.BidRepository;
import org.trinityfforce.sagopalgo.item.entity.Item;
import org.trinityfforce.sagopalgo.item.repository.ItemRepository;
import org.trinityfforce.sagopalgo.user.entity.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BidService {

    private final ItemRepository itemRepository;
    private final BidRepository bidRepository;
    private final RedisTemplate<String, HashMap<String, Object>> hashMapRedisTemplate;

    public List<BidItemResponseDto> getBidOnItem(Long itemId) {
        String bidItemHistoryKey = "Bid:Item:" + itemId;
        List<HashMap<String, Object>> bidItemHistory = hashMapRedisTemplate.opsForList().range(bidItemHistoryKey, 0 , -1);

        if (bidItemHistory == null) {
            return Collections.emptyList();
        }

        return bidItemHistory.stream().map(bidMap -> new BidItemResponseDto(
                (Long) bidMap.get("userId"),
                (Integer) bidMap.get("price")
        )).collect(Collectors.toList());
    }

    public List<BidUserResponseDto> getBidOnUser(Long userId) {
        String bidUserHistoryKey = "Bid:User:" + userId;
        List<HashMap<String, Object>> bidUserHistory = hashMapRedisTemplate.opsForList().range(bidUserHistoryKey, 0 , -1);

        if (bidUserHistory == null) {
            return Collections.emptyList();
        }

        return bidUserHistory.stream().map(bidMap -> new BidUserResponseDto(
                (Long) bidMap.get("itemId"),
                (Integer) bidMap.get("price")
        )).collect(Collectors.toList());
    }

    @Transactional
//    @WithDistributedLock(lockName = "#itemId")
    @CacheEvict(value = "item", allEntries = true)
    public void placeBid(Long itemId, User user, BidRequestDto requestDto) {
        String itemKey = "Item:" + itemId;
        Integer bidUnit = checkPrice(itemKey, itemId, requestDto.getPrice());

        // 상품의 현재가 캐싱(userId + price + bidUnit)
        HashMap<String, Object> bidInfo = new HashMap<>();
        bidInfo.put("userId", user.getId());
        bidInfo.put("price", requestDto.getPrice());
        bidInfo.put("bidUnit", bidUnit);
        hashMapRedisTemplate.opsForValue().set(itemKey, bidInfo);

        bidRepository.save(new Bid(itemId, user, requestDto.getPrice()));

        itemRepository.updateItem(itemId, requestDto.getPrice());
    }

    private Integer checkPrice(String itemKey, Long itemId, Integer price) {
        HashMap<String, Object> oldBidInfo = hashMapRedisTemplate.opsForValue().get(itemKey);
        int minimum;
        Integer bidUnit;
        if (oldBidInfo != null) {
            bidUnit = (Integer) oldBidInfo.get("bidUnit");
            minimum = (Integer) oldBidInfo.get("price") + bidUnit;
        } else {
            Item item = itemRepository.findById(itemId).orElseThrow(
                    () -> new IllegalArgumentException("해당 ID를 가진 상품은 존재하지 않습니다.")
            );
            bidUnit = item.getBidUnit();
            minimum = item.getHighestPrice() + bidUnit;
        }
        if (minimum > price) {
            throw new IllegalArgumentException("입찰가는 " + minimum + "원 이상이어야 합니다.");
        }
        return bidUnit;
    }
}
