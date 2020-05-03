package com.space.controller;

import com.space.model.ShipModel;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path = "/rest")
public class ShipController {

    @Autowired
    private final ShipService shipService;

    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping(path = "/ships")
    @ResponseBody
    public ResponseEntity<List<ShipModel>> getShips(@RequestParam(required = false) String name,
                                   @RequestParam(required = false) String planet,
                                   @RequestParam(required = false) ShipType shipType,
                                   @RequestParam(required = false) Long after,
                                   @RequestParam(required = false) Long before,
                                   @RequestParam(required = false) Boolean isUsed,
                                   @RequestParam(required = false) Double minSpeed,
                                   @RequestParam(required = false) Double maxSpeed,
                                   @RequestParam(required = false) Integer minCrewSize,
                                   @RequestParam(required = false) Integer maxCrewSize,
                                   @RequestParam(required = false) Double minRating,
                                   @RequestParam(required = false) Double maxRating,
                                   @RequestParam(required = false) Integer pageNumber,
                                   @RequestParam(required = false) Integer pageSize,
                                   @RequestParam(required = false) String order) {
        if (order == null) {
            order = "id";
        }
        if (pageNumber == null) {
            pageNumber = 0;
        }
        if (pageSize == null) {
            pageSize = 3;
        }

        String property = (order.equals("DATE") ? "prodDate" : order.toLowerCase());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, property);

        List<ShipModel> ships = shipService.getShips(name,
                planet,
                shipType,
                after, before,
                isUsed,
                minSpeed, maxSpeed,
                minCrewSize, maxCrewSize,
                minRating, maxRating,
                property,
                pageable);
        return new ResponseEntity<>(ships, HttpStatus.OK);
    }

    @GetMapping(path = "/ships/count")
    public ResponseEntity<Long> getCount() {
        Long count = shipService.count();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping(path = "/ships/{id}")
    public ResponseEntity<ShipModel> editShip(@PathVariable Long id) {
        Optional<ShipModel> shipModel = shipService.getShipById(id);
        if (!shipModel.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(shipModel.get(), HttpStatus.OK);
    }

    @PostMapping(path = "/ships")
    public ResponseEntity<ShipModel> createShip(@RequestBody ShipModel shipModel) {
        String name = shipModel.getName();
        String planet = shipModel.getPlanet();
        Date prodDate = shipModel.getProdDate();
        long prodDateMillis = (prodDate == null) ? 0 : prodDate.getTime();
        Boolean isUsed = shipModel.getUsed();
        if (isUsed == null) {
            isUsed = false;
            shipModel.setUsed(isUsed);
        }
        Double speed = shipModel.getSpeed();
        Integer crewSize = shipModel.getCrewSize();
        if (name == null ||
                name.length() > 50 ||
                name.isEmpty() ||
                planet == null ||
                planet.length() > 50 ||
                planet.isEmpty() ||
                prodDateMillis <= 0 ||
                prodDateMillis < new GregorianCalendar(2800, Calendar.JANUARY, 1).getTimeInMillis() ||
                prodDateMillis > new GregorianCalendar(3019, Calendar.JANUARY, 1).getTimeInMillis() ||
                speed == null ||
                speed < 0.01 ||
                speed > 0.99 ||
                crewSize == null ||
                crewSize < 1 ||
                crewSize > 9999) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        double k = isUsed ? 0.5 : 1;
        int currentYear = 3019;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(prodDateMillis);
        int shipProdYear = calendar.get(Calendar.YEAR);
        Double rating = (80.0 * speed * k) / (currentYear - shipProdYear + 1);
        shipModel.setRating(rating);
        shipModel = shipService.createShip(shipModel);
        return new ResponseEntity<>(shipModel, HttpStatus.OK);
    }

    @PostMapping(path = "/ships/{id}")
    public ResponseEntity<ShipModel> editShip(@PathVariable Long id,
                                              @RequestBody ShipModel updatedShip){
        Optional<ShipModel> shipModel = shipService.getShipById(id);
        if (!shipModel.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (id < 0) {
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ShipModel originalShip = shipModel.get();
        if (updatedShip.getName() != null) {
            originalShip.setName(updatedShip.getName());
        }
        if (updatedShip.getPlanet() != null) {
            originalShip.setPlanet(updatedShip.getPlanet());
        }
        if (updatedShip.getShipType() != null) {
            originalShip.setShipType(updatedShip.getShipType());
        }
        Date prodDate = updatedShip.getProdDate();
        if (prodDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(prodDate.getTime());
            Date date = calendar.getTime();
            originalShip.setProdDate(date);
        }
        originalShip.setUsed(updatedShip.getUsed());
        if (updatedShip.getSpeed() != null) {
            originalShip.setSpeed(updatedShip.getSpeed());
        }
        if (updatedShip.getCrewSize() != null) {
            originalShip.setCrewSize(updatedShip.getCrewSize());
        }
        double k = originalShip.getUsed() ? 0.5 : 1;
        int currentYear = 3019;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(originalShip.getProdDate().getTime());
        int shipProdYear = calendar.get(Calendar.YEAR);
        Double rating = (80.0 * originalShip.getSpeed() * k) / (currentYear - shipProdYear + 1);
        originalShip.setRating(rating);
        updatedShip = shipService.updateShip(originalShip);
        return new ResponseEntity<>(updatedShip, HttpStatus.OK);
    }

    @DeleteMapping(path = "/ships/{id}")
    public ResponseEntity<ShipModel> deleteShip(@PathVariable Long id) {
        Optional<ShipModel> shipModel = shipService.getShipById(id);
        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!shipModel.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        shipService.deleteShip(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
