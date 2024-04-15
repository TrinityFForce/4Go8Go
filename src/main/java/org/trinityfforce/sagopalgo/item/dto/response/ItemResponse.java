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
    private Integer bidCount;
    private Integer highestPrice;
    private String username;
    private String status;
    private Integer viewCount;
    private String url;

    public ItemResponse(Item item){
        this.id = item.getId();
        this.name = item.getName();
        this.bidCount = item.getBidCount();
        this.highestPrice = item.getHighestPrice();
        this.username = item.getUser().getUsername();
        this.status = item.getStatus().getLabel();
        this.viewCount = item.getViewCount();
        this.url = item.getUrl();
    }
}
