package org.trinityfforce.sagopalgo.item.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm")
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
