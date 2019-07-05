package com.scarawooo.dto;

import com.scarawooo.encoder.Encoder;
import java.io.Serializable;

public class ClientDTO implements Serializable {
    private int id;
    private String name;
    private String surname;
    private String login;
    private byte[] pass;

    public ClientDTO(String name, String surname, String login, String pass) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.pass = Encoder.encode(pass);
    }

    public ClientDTO(int id, String name, String surname, String login, byte[] pass) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.pass = pass;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getLogin() {
        return login;
    }

    public byte[] getPass() {
        return pass;
    }
}