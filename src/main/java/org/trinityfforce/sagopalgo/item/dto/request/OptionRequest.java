package org.trinityfforce.sagopalgo.item.dto.request;

import lombok.Data;

@Data
public class OptionRequest {

    private int page;
    private int size;
    private String option;
    private boolean ASC;
}
