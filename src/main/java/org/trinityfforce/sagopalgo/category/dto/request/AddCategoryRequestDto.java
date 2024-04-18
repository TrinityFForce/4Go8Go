package org.trinityfforce.sagopalgo.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddCategoryRequestDto {

    @NotBlank(message = "카테고리명은 필수 항목입니다")
    private String name;
}
