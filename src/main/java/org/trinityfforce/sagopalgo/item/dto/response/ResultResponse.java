package org.trinityfforce.sagopalgo.item.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResultResponse {

    private Integer code;
    private String status;
    private String msg;

}
