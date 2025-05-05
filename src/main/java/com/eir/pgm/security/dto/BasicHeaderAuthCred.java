package com.eir.pgm.security.dto;

import lombok.Data;

@Data
public class BasicHeaderAuthCred {

    private String username;

    private String password;

    public BasicHeaderAuthCred(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
