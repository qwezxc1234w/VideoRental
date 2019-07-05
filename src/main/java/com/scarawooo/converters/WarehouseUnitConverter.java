package com.scarawooo.converters;

import com.scarawooo.dto.WarehouseUnitDTO;
import com.scarawooo.entity.WarehouseUnitEntity;

public class WarehouseUnitConverter {
    public static WarehouseUnitEntity convert(WarehouseUnitDTO warehouseUnitDTO) {
        return new WarehouseUnitEntity(warehouseUnitDTO.getId(), FilmConverter.convert(warehouseUnitDTO.getFilm()),
                warehouseUnitDTO.getAmount());
    }

    public static WarehouseUnitDTO convert(WarehouseUnitEntity warehouseUnitEntity) {
        return new WarehouseUnitDTO(warehouseUnitEntity.getId(), FilmConverter.convert(warehouseUnitEntity.getFilm()),
                warehouseUnitEntity.getAmount());
    }
}
