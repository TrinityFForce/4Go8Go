package org.trinityfforce.sagopalgo.item.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemRequest {

    @NotBlank
    private String name;

    @NotBlank
    @PositiveOrZero
    private Integer startPrice;

    @NotBlank
    @PositiveOrZero
    private Integer bidUnit;

    @NotBlank
    @Future
    private LocalDateTime deadLine;

    @NotBlank
    private String category;
}
