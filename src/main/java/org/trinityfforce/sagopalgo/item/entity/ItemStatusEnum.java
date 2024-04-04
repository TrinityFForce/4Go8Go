package org.trinityfforce.sagopalgo.item.entity;

public enum ItemStatusEnum {
    PENDING("PENDING"), INPROGRESS("INPROGRESS"), COMPLETED("COMPLETED");
    private final String label;

    ItemStatusEnum(String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }

}
