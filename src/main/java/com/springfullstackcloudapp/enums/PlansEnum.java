package com.springfullstackcloudapp.enums;

/**
 * Created by ranji on 5/19/2017.
 */
public enum PlansEnum {

    BASIC(1,"Basic"),
    PRO(2,"Pro");

    private final int id;
    private final String PlanName;

    PlansEnum(int id, String planName) {
        this.id = id;
        PlanName = planName;
    }

    public int getId() {
        return id;
    }

    public String getPlanName() {
        return PlanName;
    }
}
