package one.digitalinnovation.parking.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import one.digitalinnovation.parking.dto.dto.ParkingDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import one.digitalinnovation.parking.exception.ParkingNotFoundException;
import one.digitalinnovation.parking.model.Parking;
import one.digitalinnovation.parking.repository.ParkingRepository;

@Service
public class ParkingService {

    private final ParkingRepository parkingRepository;

    private final ModelMapper modelMapper;

    public ParkingService(ParkingRepository parkingRepository, ModelMapper modelMapper) {
        this.parkingRepository = parkingRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public List<ParkingDTO> findAll() {
        List<Parking> parkingList = parkingRepository.findAll();
        return parkingList.stream().map(parkingEntity -> modelMapper.map(parkingEntity, ParkingDTO.class)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ParkingDTO findById(String id) {
        Parking parking = parkingRepository.findById(id).orElseThrow(() -> new ParkingNotFoundException());
        return modelMapper.map(parking, ParkingDTO.class);
    }

    @Transactional
    public ParkingDTO create(ParkingDTO parkingCreate) {
        Parking parking = modelMapper.map(parkingCreate, Parking.class);
        String uuid = getUUID();
        parking.setId(uuid);
        Parking parkingSave = parkingRepository.save(parking);
        return modelMapper.map(parkingSave, ParkingDTO.class);
    }

    @Transactional
    public void delete(String id) {
        var parking = parkingRepository.findById(id)
                .orElseThrow(ParkingNotFoundException::new);
        parkingRepository.delete(parking);
    }

    @Transactional
    public ParkingDTO update(String id, ParkingDTO parkingCreate) {
        Parking parking = parkingRepository.findById(id).orElseThrow(ParkingNotFoundException::new);
        parkingRepository.save(parking);
        return modelMapper.map(parking, ParkingDTO.class);
    }

    @Transactional
    public Parking checkOut(String id) {
        Parking parking = parkingRepository.findById(id).orElseThrow(ParkingNotFoundException::new);
        parking.setExitDate(LocalDateTime.now());
        parking.setBill(ParkingCheckOut.getBill(parking));
        return parkingRepository.save(parking);
    }

    private static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
