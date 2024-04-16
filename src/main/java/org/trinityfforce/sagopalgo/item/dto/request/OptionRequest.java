package org.trinityfforce.sagopalgo.item.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptionRequest {
    private String name;
    private String category;
    private String status;
    private int page;
    private int size;
    private String option;
    private boolean ASC;
}
