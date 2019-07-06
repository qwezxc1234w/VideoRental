package com.scarawooo.dao;

import com.scarawooo.dto.WarehouseUnitDTO;
import java.util.List;

public interface WarehouseUnitDAO {
    WarehouseUnitDTO getById(int id);

    List<WarehouseUnitDTO> getAll();

    void update(WarehouseUnitDTO warehouseUnitDTO);
}
