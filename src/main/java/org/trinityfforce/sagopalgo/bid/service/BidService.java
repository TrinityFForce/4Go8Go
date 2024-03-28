package org.trinityfforce.sagopalgo.bid.service;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trinityfforce.sagopalgo.bid.dto.BidRequestDto;
import org.trinityfforce.sagopalgo.bid.dto.BidResponseDto;
import org.trinityfforce.sagopalgo.bid.entity.Bid;
import org.trinityfforce.sagopalgo.bid.repository.BidRepository;
import org.trinityfforce.sagopalgo.global.anotation.WithDistributedLock;
import org.trinityfforce.sagopalgo.item.entity.Item;
import org.trinityfforce.sagopalgo.item.repository.ItemRepository;
import org.trinityfforce.sagopalgo.user.entity.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BidService {

    private final BidRepository bidRepository;
    private final ItemRepository itemRepository;
    private final RedissonClient redissonClient;

    public List<BidResponseDto> getBidOnItem(Long itemId) {
        return bidRepository.findAllByItemId(itemId).stream().map(BidResponseDto::new).toList();
    }

    public List<BidResponseDto> getBidOnUser(Long userId) {
        return bidRepository.findAllByUserId(userId).stream().map(BidResponseDto::new).toList();
    }

    @Transactional
    @WithDistributedLock(lockName = "#itemId")
    public void placeBid(Long itemId, User user, BidRequestDto requestDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("해당 ID를 가진 상품은 존재하지 않습니다.")
        );

        checkPrice(item, requestDto.getPrice());

        Bid bid = bidRepository.findByUserIdAndItemId(user.getId(), item.getId());
        if (bid == null) {
            bidRepository.save(new Bid(item, user, requestDto.getPrice()));
        } else {
            bid.updatePrice(requestDto.getPrice());
        }
        item.updateBidItem(requestDto.getPrice());
    }

    private void checkPrice(Item item, Integer price) {
        int minimum = item.getHighestPrice() + item.getBidUnit();
        if (minimum > price) {
            throw new IllegalArgumentException("입찰가는 " + minimum + "원 이상이어야 합니다!");
        }
    }
}
