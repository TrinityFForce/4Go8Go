package org.trinityfforce.sagopalgo.bid.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BidItemResponseDto {

    private Long userId;
    private Integer price;

    public BidItemResponseDto(Long userId, Integer price) {
        this.userId = userId;
        this.price = price;
    }
}
