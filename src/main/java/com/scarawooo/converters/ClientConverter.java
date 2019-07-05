package com.scarawooo.converters;

import com.scarawooo.dto.ClientDTO;
import com.scarawooo.entity.ClientEntity;

public class ClientConverter {
    public static ClientEntity convert(ClientDTO clientDTO) {
        return new ClientEntity(clientDTO.getId(), clientDTO.getName(), clientDTO.getSurname(), clientDTO.getLogin(), clientDTO.getPass());
    }

    public static ClientDTO convert(ClientEntity clientEntity) {
        return new ClientDTO(clientEntity.getId(), clientEntity.getName(), clientEntity.getSurname(), clientEntity.getLogin(), clientEntity.getPass());
    }
}
