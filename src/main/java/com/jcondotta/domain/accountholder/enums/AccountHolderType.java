package com.jcondotta.domain.accountholder.enums;

public enum AccountHolderType {
    PRIMARY, JOINT;

    public boolean isPrimary() {
        return this == PRIMARY;
    }

    public boolean isJoint() {
        return this == JOINT;
    }
}
