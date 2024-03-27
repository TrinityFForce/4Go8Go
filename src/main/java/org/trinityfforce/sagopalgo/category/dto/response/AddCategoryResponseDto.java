package org.trinityfforce.sagopalgo.category.dto.response;

import lombok.Getter;
import org.trinityfforce.sagopalgo.category.entity.Category;

@Getter
public class AddCategoryResponseDto {

    private Long id;
    private String name;

    public AddCategoryResponseDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }
}
