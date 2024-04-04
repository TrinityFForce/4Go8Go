package org.trinityfforce.sagopalgo.bid.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.trinityfforce.sagopalgo.bid.entity.Bid;

@Getter
@NoArgsConstructor
public class BidResponseDto {

    private Long id;
    private Integer price;

    public BidResponseDto(Bid bid) {
        this.id = bid.getId();
        this.price = bid.getPrice();
    }
}