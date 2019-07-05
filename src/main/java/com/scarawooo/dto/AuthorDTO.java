package com.scarawooo.dto;

import java.io.Serializable;

public class AuthorDTO implements Serializable {
    private String name;

    public AuthorDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}