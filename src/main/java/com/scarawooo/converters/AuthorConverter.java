package com.scarawooo.converters;

import com.scarawooo.dto.AuthorDTO;
import com.scarawooo.entity.AuthorEntity;

public class AuthorConverter {
    public static AuthorEntity convert(AuthorDTO authorDTO) {
        return new AuthorEntity(authorDTO.getName());
    }

    public static AuthorDTO convert(AuthorEntity authorEntity) {
        return new AuthorDTO(authorEntity.getName());
    }
}
