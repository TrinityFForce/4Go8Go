package org.trinityfforce.sagopalgo.bid.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trinityfforce.sagopalgo.bid.dto.BidRequestDto;
import org.trinityfforce.sagopalgo.bid.dto.BidResponseDto;
import org.trinityfforce.sagopalgo.bid.dto.ItemPriceUpdate;
import org.trinityfforce.sagopalgo.bid.entity.Bid;
import org.trinityfforce.sagopalgo.bid.repository.BidRepository;
import org.trinityfforce.sagopalgo.item.entity.Item;
import org.trinityfforce.sagopalgo.item.repository.ItemRepository;
import org.trinityfforce.sagopalgo.item.service.ItemService;
import org.trinityfforce.sagopalgo.user.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BidService {

    private final ItemRepository itemRepository;
    private final BidRepository bidRepository;
    private final RedisTemplate<String, HashMap<String, Object>> hashMapRedisTemplate;
    public List<BidResponseDto> getBidOnItem(Long itemId) {
        return bidRepository.findAllByItemId(itemId).stream().map(BidResponseDto::new).toList();
    }

    public List<BidResponseDto> getBidOnUser(Long userId) {
        return bidRepository.findAllByUserId(userId).stream().map(BidResponseDto::new).toList();
    }

    @Transactional
    public void placeBid(Long itemId, User user, BidRequestDto requestDto)
        throws BadRequestException {
        String itemKey = "Item:" + itemId;
        Integer bidUnit = checkPrice(itemKey, itemId, requestDto.getPrice());

        // 상품의 현재가 캐싱(userId + price + bidUnit)
        HashMap<String, Object> bidInfo = new HashMap<>();
        bidInfo.put("userId", user.getId());
        bidInfo.put("price", requestDto.getPrice());
        bidInfo.put("bidUnit", bidUnit);

        // TTL 설정
        Long ttl = hashMapRedisTemplate.getExpire(itemKey, TimeUnit.SECONDS);
        if (ttl != null && (ttl == -1 || ttl == -2)) {
            hashMapRedisTemplate.opsForValue().set(itemKey, bidInfo, 12 * 60 * 60, TimeUnit.SECONDS);
        } else if (ttl != null){
            hashMapRedisTemplate.opsForValue().set(itemKey, bidInfo, ttl, TimeUnit.SECONDS);
        }

        hashMapRedisTemplate.convertAndSend("itemPriceUpdate", new ItemPriceUpdate(itemId, requestDto.getPrice()));

        bidRepository.save(new Bid(itemId, user, requestDto.getPrice()));
        itemRepository.updateItem(itemId, requestDto.getPrice());
    }

    private Integer checkPrice(String itemKey, Long itemId, Integer price)
        throws BadRequestException {
        HashMap<String, Object> oldBidInfo = hashMapRedisTemplate.opsForValue().get(itemKey);
        int minimum;
        Integer bidUnit;
        if (oldBidInfo != null) {
            bidUnit = (Integer) oldBidInfo.get("bidUnit");
            minimum = (Integer) oldBidInfo.get("price") + bidUnit;
        } else {
            Item item = itemRepository.findById(itemId).orElseThrow(
                    () -> new BadRequestException("해당 ID를 가진 상품은 존재하지 않습니다.")
            );
            if (!Objects.equals(item.getStatus().getLabel(), "INPROGRESS")) {
                throw new BadRequestException("경매중인 상품만 입찰이 가능합니다.");
            }
            bidUnit = item.getBidUnit();
            minimum = item.getHighestPrice() + bidUnit;
        }
        if (minimum > price) {
            throw new BadRequestException("입찰가는 " + minimum + "원 이상이어야 합니다.");
        }
        return bidUnit;
    }
}
