package org.trinityfforce.sagopalgo.item.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RelistRequest {

    @NotNull(message = "경매 시작일은 필수 항목입니다.")
    @Future(message = "경매 시작일은 미래여야 합니다.")
    private LocalDate startDate;
}
