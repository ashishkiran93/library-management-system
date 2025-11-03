package com.ashish.library_management_system.model;

public enum MembershipType {
    BASIC(5),
    PREMIMIUM(2);

    private final int limit;

    MembershipType(int limit){
        this.limit=limit;
    }

    public int getMaxLimit(){
        return limit;
    }
}
