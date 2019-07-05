package com.scarawooo.converters;

import com.scarawooo.dto.ReserveUnitDTO;
import com.scarawooo.entity.ReserveUnitEntity;

public class ReserveUnitConverter {
    public static ReserveUnitEntity convert(ReserveUnitDTO reserveUnitDTO) {
        return new ReserveUnitEntity(reserveUnitDTO.getId(), ClientConverter.convert(reserveUnitDTO.getClient()),
                WarehouseUnitConverter.convert(reserveUnitDTO.getWarehouseUnit()),
                reserveUnitDTO.getAmount(), reserveUnitDTO.getBeginDate(), reserveUnitDTO.getEndDate());
    }

    public static ReserveUnitDTO convert(ReserveUnitEntity reserveUnitEntity) {
        return new ReserveUnitDTO(reserveUnitEntity.getId(), ClientConverter.convert(reserveUnitEntity.getClient()),
                WarehouseUnitConverter.convert(reserveUnitEntity.getWarehouseUnit()),
                reserveUnitEntity.getAmount(), reserveUnitEntity.getBeginDate(), reserveUnitEntity.getEndDate());
    }
}
