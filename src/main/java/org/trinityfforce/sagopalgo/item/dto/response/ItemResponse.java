package org.trinityfforce.sagopalgo.item.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.trinityfforce.sagopalgo.item.entity.Item;

@Getter
@AllArgsConstructor
public class ItemResponse {

    private Long id;
    private String name;
    private Integer startPrice;
    private Integer bidCount;
    private Integer bidUnit;
    private LocalDateTime deadline;
    private Integer highestPrice;
    private String category;
    private String username;

    public ItemResponse(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.startPrice = item.getStartPrice();
        this.bidCount = item.getBidCount();
        this.deadline = item.getDeadline();
        this.highestPrice = item.getHighestPrice();
        this.category = item.getCategory().getName();
        this.username = item.getUser().getUsername();
        this.bidUnit = item.getBidUnit();
    }
}
