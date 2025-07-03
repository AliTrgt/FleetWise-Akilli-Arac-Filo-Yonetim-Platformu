package com.example.DriverService.service;

import com.example.DriverService.dto.driverpenalty.request.InsertDriverPenalty;
import com.example.DriverService.dto.driverpenalty.request.UpdateDriverPenalty;
import com.example.DriverService.dto.driverpenalty.response.DriverPenaltyViewModel;

import java.util.List;
import java.util.UUID;

public interface IDriverPenaltyService {

    List<DriverPenaltyViewModel> getAllPenalty(UUID driverId) throws Exception;

    void insert(InsertDriverPenalty driverPenalty) throws Exception;

    void update(UUID penaltyId,UpdateDriverPenalty driverPenalty) throws Exception;

    void delete(UUID id) throws Exception;


}
