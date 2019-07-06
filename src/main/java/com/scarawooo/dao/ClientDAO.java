package com.scarawooo.dao;

import com.scarawooo.dto.ClientDTO;

public interface ClientDAO {
    boolean isLoginOccupy(ClientDTO clientDTO);

    void insert(ClientDTO clientDTO);

    ClientDTO getByLoginAndPass(ClientDTO clientDTO);
}
