package org.trinityfforce.sagopalgo.category.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModifyCategoryRequestDto {

    @NotEmpty(message = "빈 문자열은 허용하지 않습니다.")
    private String name;
}
