package com.scarawooo.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "clients")
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private int id;

    @Column(name = "client_name")
    private String name;

    @Column(name = "client_surname")
    private String surname;

    @Column(name = "client_login")
    private String login;

    @Column(name = "client_pass")
    private byte[] pass;

    @OneToMany(mappedBy = "client")
    private Set<ReserveUnitEntity> reserves;

    public ClientEntity() {}

    public ClientEntity(int id, String name, String surname, String login, byte[] pass) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.surname = surname;
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
