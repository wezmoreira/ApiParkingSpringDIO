package one.digitalinnovation.parking.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import one.digitalinnovation.parking.dto.dto.ParkingCreateDTO;
import one.digitalinnovation.parking.dto.dto.ParkingDTO;
import one.digitalinnovation.parking.model.Parking;
import one.digitalinnovation.parking.service.ParkingService;

@RestController
@RequestMapping("/parking")
@Api(tags = "Parking Controller")
public class ParkingController {

    private final ParkingService parkingService;

    private final ModelMapper modelMapper;

    public ParkingController(ParkingService parkingService, ModelMapper modelMapper) {
        this.parkingService = parkingService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @ApiOperation("Find all parkings")
    public ResponseEntity<List<ParkingDTO>> findAll() {
        List<ParkingDTO> parkingDTOList = parkingService.findAll();
        return ResponseEntity.ok(parkingDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingDTO> findById(@PathVariable String id) {
        ParkingDTO parkingDTO = parkingService.findById(id);
        return ResponseEntity.ok(parkingDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        parkingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<ParkingDTO> create(@RequestBody ParkingCreateDTO dto) {
        var model = modelMapper.map(dto, ParkingDTO.class);
        ParkingDTO parkingDTO = parkingService.create(model);
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ParkingDTO> update(@PathVariable String id, @RequestBody ParkingCreateDTO parkingCreteDTO) {
        var model = modelMapper.map(parkingCreteDTO, ParkingDTO.class);
        ParkingDTO parkingDTO = parkingService.update(id, model);
        return ResponseEntity.ok(parkingDTO);
    }

    @PostMapping("/{id}/exit")
    public ResponseEntity<ParkingDTO> checkOut(@PathVariable String id) {
        Parking parking = parkingService.checkOut(id);
        var model = modelMapper.map(parking, ParkingDTO.class);
        return ResponseEntity.ok(model);
    }

}
