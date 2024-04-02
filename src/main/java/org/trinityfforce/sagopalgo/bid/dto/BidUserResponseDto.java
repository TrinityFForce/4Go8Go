package org.trinityfforce.sagopalgo.bid.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BidUserResponseDto {

    private Long itemId;
    private Integer price;

    public BidUserResponseDto(Long itemId, Integer price) {
        this.itemId = itemId;
        this.price = price;
    }
}
