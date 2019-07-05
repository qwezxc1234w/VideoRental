package com.scarawooo.dto;

import java.io.Serializable;

public class GenreDTO implements Serializable {
    private String name;

    public GenreDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
