package com.amc.api.Enums;

public enum UserRoleEnum {

    ADMINISTRATOR("administrador"),
    MANAGER("supervisor"),
    SELLER("vendedor"),
    USER("usuario");

    private String role;

    UserRoleEnum(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
