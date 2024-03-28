package org.trinityfforce.sagopalgo.item.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemRequest {

    @NotBlank(message = "상품 이름은 필수 항목입니다.")
    private String name;

    @NotNull(message = "최소 입찰액은 필수 항목입니다.")
    @PositiveOrZero(message = "최소 입찰액은 0원 이거나 커야합니다.")
    private Integer startPrice;

    @NotNull(message = "입찰단위는 필수 항목입니다.")
    @PositiveOrZero(message = "입찰단위는 0원 이거나 커야합니다.")
    private Integer bidUnit;

    @NotNull(message = "경매 종료일은 필수 항목입니다.")
    @Future(message = "경매 종료일은 미래여야 합니다.")
    private LocalDateTime deadLine;

    @NotBlank(message = "카테고리는 필수 항목입니다.")
    private String category;
}
