package com.assignment.entity;

public enum Role {
    STUDENT("ROLE_STUDENT", "학생"),
    TEACHER("ROLE_TEACHER", "선생");

    private final String authority;
    private final String displayName;

    Role(String authority, String displayName) {
        this.authority = authority;
        this.displayName = displayName;
    }

    public String getAuthority() {
        return authority;
    }

    public String getDisplayName() {
        return displayName;
    }
}
