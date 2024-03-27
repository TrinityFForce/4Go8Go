package org.trinityfforce.sagopalgo.category.dto.response;

import lombok.Getter;
import org.trinityfforce.sagopalgo.category.entity.Category;

@Getter
public class GetCategorysResponseDto {

    private Long id;
    private String name;

    public GetCategorysResponseDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }
}
