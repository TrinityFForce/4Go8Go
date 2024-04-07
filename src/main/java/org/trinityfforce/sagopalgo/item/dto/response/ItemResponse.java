package org.trinityfforce.sagopalgo.item.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.trinityfforce.sagopalgo.item.entity.Item;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponse {

    private Long id;
    private String name;
    private Integer startPrice;
    private Integer bidCount;
    private Integer bidUnit;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startDate;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime deadline;
    private Integer highestPrice;
    private String category;
    private String username;
    private String status;
    private Integer viewCount;
    private String url;

    public ItemResponse(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.startPrice = item.getStartPrice();
        this.bidCount = item.getBidCount();
        this.startDate = item.getStartDate().atStartOfDay().plusHours(9);
        this.deadline = item.getStartDate().atStartOfDay().plusHours(18);
        this.highestPrice = item.getHighestPrice();
        this.category = item.getCategory().getName();
        this.username = item.getUser().getUsername();
        this.bidUnit = item.getBidUnit();
        this.status = item.getStatus().getLabel();
        this.viewCount = item.getViewCount();
        this.url = item.getUrl();
    }
}
