package com.untistore.clans.models;

public enum Role {
    OWNER(),
    MANAGER(),
    STAFF(),
    MEMBER(),
    ;

    public boolean isAtLeast(Role role) {
        return this.ordinal() <= role.ordinal();
    }

    public Role getNext() {
        return values()[(this.ordinal() + 1) % values().length];
    }
}
