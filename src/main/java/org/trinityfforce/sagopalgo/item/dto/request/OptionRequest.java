package org.trinityfforce.sagopalgo.item.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptionRequest {

    private int page;
    private int size;
    private String option;
    private boolean ASC;
}
