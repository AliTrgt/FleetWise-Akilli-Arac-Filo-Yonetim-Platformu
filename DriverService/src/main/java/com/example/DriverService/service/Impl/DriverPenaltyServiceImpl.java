package com.example.DriverService.service.Impl;

import com.example.DriverService.dto.driverpenalty.request.InsertDriverPenalty;
import com.example.DriverService.dto.driverpenalty.request.UpdateDriverPenalty;
import com.example.DriverService.dto.driverpenalty.response.DriverPenaltyViewModel;
import com.example.DriverService.entity.DriverPenalty;
import com.example.DriverService.repository.IDriverPenaltyRepository;
import com.example.DriverService.service.IDriverPenaltyService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DriverPenaltyServiceImpl implements IDriverPenaltyService {
    private static final Logger _logger = LoggerFactory.getLogger(DriverPenaltyServiceImpl.class);
    private final IDriverPenaltyRepository penalty;
    private final ModelMapper modelMapper;

    public DriverPenaltyServiceImpl(IDriverPenaltyRepository penalty, ModelMapper modelMapper) {
        this.penalty = penalty;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<DriverPenaltyViewModel> getAllPenalty(UUID driverId) {
        _logger.info("Fetched penalty by driverId : {} ", driverId);
        return penalty.findAllByDriver_IdAndDeletedFalse(driverId)
                    .stream()
                    .map(penalty -> modelMapper.map(penalty, DriverPenaltyViewModel.class))
                    .collect(Collectors.toList());
    }

    @Override
    public void insert(InsertDriverPenalty driverPenalty) throws Exception {
            try {
                    DriverPenalty dPenalty = modelMapper.map(driverPenalty, DriverPenalty.class);
                    penalty.save(dPenalty);
                    _logger.info("DriverPenalty : {} successfully inserted",dPenalty);
            }catch (Exception e){
                _logger.error("An error occurred while insert driver penalty .",e);
                throw new Exception(e);
            }
    }

    @Override
    public void update(UUID penaltyId,UpdateDriverPenalty driverPenalty) throws Exception {
        try {
            DriverPenalty dPenalty = penalty.findByIdAndDeletedFalse(penaltyId)
                    .orElseThrow(() ->new NoSuchElementException(penaltyId + " ID'li sürücü cezası bulunamadı"));

            modelMapper.map(driverPenalty,dPenalty);

            penalty.save(dPenalty);
            _logger.info("DriverPenalty : {} successfully updated id : {}",dPenalty,penaltyId);

        }catch (Exception e){
            _logger.error("An error occurred while update driver penalty .",e);

            throw new Exception(e);
        }
    }

    @Override
    public void delete(UUID id) throws Exception {
            try {
                DriverPenalty driverPenalty = penalty.findById(id).orElseThrow(() ->new NoSuchElementException(id+" ID'li eleman bulunamadı"));
                driverPenalty.setDeleted(true);
                penalty.save(driverPenalty);
                _logger.info("DriverPenalty : {} successfully deleted id : {}",driverPenalty,id);

            }catch (Exception e){
                _logger.error("An error occurred while delete driver penalty .",e);
                throw new Exception(e);
            }
    }
}
