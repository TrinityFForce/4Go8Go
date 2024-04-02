package org.trinityfforce.sagopalgo.bid.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.trinityfforce.sagopalgo.bid.dto.BidRequestDto;
import org.trinityfforce.sagopalgo.bid.dto.BidItemResponseDto;
import org.trinityfforce.sagopalgo.bid.dto.BidUserResponseDto;
import org.trinityfforce.sagopalgo.bid.service.BidService;
import org.trinityfforce.sagopalgo.global.common.CommonResponseDto;
import org.trinityfforce.sagopalgo.global.security.UserDetailsImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BidController {

    private final BidService bidService;

    @GetMapping("/items/{itemId}/bid-history")
    public ResponseEntity<List<BidItemResponseDto>> getBidOnItem(@PathVariable Long itemId) {
        List<BidItemResponseDto> bidItemResponseDtoList = bidService.getBidOnItem(itemId);
        return ResponseEntity.status(200).body(bidItemResponseDtoList);
    }

    @GetMapping("/users/bid")
    public ResponseEntity<List<BidUserResponseDto>> getBidOnUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<BidUserResponseDto> bidUserResponseDtoList = bidService.getBidOnUser(userDetails.getUser().getId());
        return ResponseEntity.status(200).body(bidUserResponseDtoList);
    }

    @PostMapping("/items/{itemId}/bid")
    public ResponseEntity<CommonResponseDto> placeBid(@PathVariable Long itemId,
                                                      @RequestBody BidRequestDto requestDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        bidService.placeBid(itemId, userDetails.getUser(), requestDto);
        return ResponseEntity.status(201).body(new CommonResponseDto("입찰 성공", 201));
    }
}
