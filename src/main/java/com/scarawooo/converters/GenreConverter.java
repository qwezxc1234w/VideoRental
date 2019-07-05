package com.scarawooo.converters;

import com.scarawooo.dto.GenreDTO;
import com.scarawooo.entity.GenreEntity;

public class GenreConverter {
    public static GenreEntity convert(GenreDTO genreDTO) {
        return new GenreEntity(genreDTO.getName());
    }

    public static GenreDTO convert(GenreEntity genreEntity) {
        return new GenreDTO(genreEntity.getName());
    }
}
