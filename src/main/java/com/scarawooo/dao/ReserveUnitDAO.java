package com.scarawooo.dao;

import com.scarawooo.dto.ClientDTO;
import com.scarawooo.dto.ReserveUnitDTO;
import java.util.List;

public interface ReserveUnitDAO {
    List<ReserveUnitDTO> getClientReserves(ClientDTO clientDTO);

    void insert(ReserveUnitDTO reserveUnitDTO);

    void delete(ReserveUnitDTO reserveUnitDTO);
}
