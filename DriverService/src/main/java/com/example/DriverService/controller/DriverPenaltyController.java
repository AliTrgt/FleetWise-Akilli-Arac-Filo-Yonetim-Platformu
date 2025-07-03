package com.example.DriverService.controller;

import com.example.DriverService.dto.driverpenalty.request.InsertDriverPenalty;
import com.example.DriverService.dto.driverpenalty.request.UpdateDriverPenalty;
import com.example.DriverService.dto.driverpenalty.response.DriverPenaltyViewModel;
import com.example.DriverService.service.IDriverPenaltyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/driverPenalty")
@RequiredArgsConstructor
public class DriverPenaltyController {

    private final IDriverPenaltyService driverPenaltyService;
    private final ModelMapper modelMapper;


    @GetMapping
    public ResponseEntity<?> getAllByDriverId(@RequestParam UUID driverId) throws Exception {
        List<DriverPenaltyViewModel> driverPenaltyViewModels =
                driverPenaltyService.getAllPenalty(driverId)
                        .stream()
                        .map(penalty -> modelMapper.map(penalty, DriverPenaltyViewModel.class))
                        .collect(Collectors.toList());

                return ResponseEntity.ok(driverPenaltyViewModels);
    }

    @PostMapping
    public ResponseEntity<?> insert(@RequestBody InsertDriverPenalty penalty) throws Exception{
            driverPenaltyService.insert(penalty);
            return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody UpdateDriverPenalty penalty) throws Exception{
            driverPenaltyService.update(id,penalty);
            return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) throws Exception{
            driverPenaltyService.delete(id);
            return ResponseEntity.ok().build();
    }


}
