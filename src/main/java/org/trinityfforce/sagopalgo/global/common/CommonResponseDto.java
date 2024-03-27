package org.trinityfforce.sagopalgo.global.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResponseDto {

    private String msg;
    private Integer statusCode;

    public CommonResponseDto(String msg, Integer statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
