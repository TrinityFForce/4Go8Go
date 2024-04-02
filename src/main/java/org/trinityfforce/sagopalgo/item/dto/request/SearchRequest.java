package org.trinityfforce.sagopalgo.item.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class SearchRequest {

    private String name;
    private String category;

}
