package com.scarawooo.converters;

import com.scarawooo.dto.FilmDTO;
import com.scarawooo.entity.FilmEntity;

public class FilmConverter {
    public static FilmEntity convert(FilmDTO filmDTO) {
        return new FilmEntity(filmDTO.getId(), filmDTO.getName(), AuthorConverter.convert(filmDTO.getAuthor()),
                GenreConverter.convert(filmDTO.getGenre()), filmDTO.getPrice());
    }

    public static FilmDTO convert(FilmEntity filmEntity) {
        return new FilmDTO(filmEntity.getId(), filmEntity.getName(), AuthorConverter.convert(filmEntity.getAuthor()),
                GenreConverter.convert(filmEntity.getGenre()), filmEntity.getPrice());
    }
}
