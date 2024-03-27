package org.trinityfforce.sagopalgo.bid.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.trinityfforce.sagopalgo.bid.dto.BidRequestDto;
import org.trinityfforce.sagopalgo.bid.dto.BidResponseDto;
import org.trinityfforce.sagopalgo.bid.service.BidService;
import org.trinityfforce.sagopalgo.global.common.CommonResponseDto;
import org.trinityfforce.sagopalgo.user.entity.User;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BidController {

    private final BidService bidService;

    @GetMapping("/items/{itemId}/bid-history")
    public ResponseEntity<List<BidResponseDto>> getBidOnItem(@PathVariable Long itemId) {
        List<BidResponseDto> bidResponseDtoList = bidService.getBidOnItem(itemId);
        return ResponseEntity.status(200).body(bidResponseDtoList);
    }

    @GetMapping("/users/bid")
    public ResponseEntity<List<BidResponseDto>> getBidOnUser() {
        User user = new User(1L, "mock", "mock", "mock"); // 모의 처리
        List<BidResponseDto> bidResponseDtoList = bidService.getBidOnUser(user.getId());
        return ResponseEntity.status(200).body(bidResponseDtoList);
    }

    @PostMapping("/items/{itemId}/bid")
    public ResponseEntity<CommonResponseDto> placeBid(@PathVariable Long itemId,
                                                      @RequestBody BidRequestDto requestDto) {
        User user = new User(1L, "fdsa", "fdsa", "fdsa"); // 모의 처리
        bidService.placeBid(itemId, user, requestDto);
        return ResponseEntity.status(201).body(new CommonResponseDto("입찰 성공", 201));
    }
}
