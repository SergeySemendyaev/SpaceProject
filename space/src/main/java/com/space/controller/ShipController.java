package com.space.controller;

import com.space.model.ShipModel;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@RestController
@RequestMapping(path = "/rest")
public class ShipController {

    private final ShipService shipService;

    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping(path = "/ships")
    @ResponseBody
    public ResponseEntity<List<ShipModel>> getShips(
            @RequestParam(required = false) String name,
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
            @RequestParam(required = false) String order
    ) {
        ShipOrder shipOrder;
        if (order == null) {
            shipOrder = ShipOrder.ID;
        }
        else {
            shipOrder = ShipOrder.valueOf(order);
        }
        if (pageNumber == null) {
            pageNumber = 0;
        }
        if (pageSize == null) {
            pageSize = 3;
        }

        String property = shipOrder.getFieldName();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, property);

        List<ShipModel> ships = shipService.getShips(
                name,
                planet,
                shipType,
                after, before,
                isUsed,
                minSpeed, maxSpeed,
                minCrewSize, maxCrewSize,
                minRating, maxRating,
                property,
                pageable
        );

        return new ResponseEntity<>(ships, HttpStatus.OK);
    }

    @GetMapping(path = "/ships/count")
    public ResponseEntity<Long> getCount(
            @RequestParam(required = false) String name,
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
            @RequestParam(required = false) Double maxRating
    ) {
        Long count = shipService.count(
                name,
                planet,
                shipType,
                after,
                before,
                isUsed,
                minSpeed,
                maxSpeed,
                minCrewSize,
                maxCrewSize,
                minRating,
                maxRating
        );

        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping(path = "/ships/{id}")
    public ResponseEntity<ShipModel> editShip(@PathVariable Long id) {
        Optional<ShipModel> shipModel = shipService.getShipById(id);
        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return shipModel.map(model -> new ResponseEntity<>(model, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
            shipModel.setUsed(false);
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
                                              @RequestBody ShipModel updatedShip) {
        if (id <= 0) {
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<ShipModel> shipModel = shipService.getShipById(id);
        if (!shipModel.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String name = updatedShip.getName();
        String planet = updatedShip.getPlanet();
        Date prodDate = updatedShip.getProdDate();
        Double speed = updatedShip.getSpeed();
        Integer crewSize = updatedShip.getCrewSize();

        boolean requestBodyIsEmpty = (
                updatedShip.getId() == null && name == null &&
                        planet == null && updatedShip.getShipType() == null &&
                        prodDate == null && updatedShip.getUsed() == null &&
                        speed == null && crewSize == null
        );
        if (requestBodyIsEmpty) {
            return new ResponseEntity<>(shipModel.get(), HttpStatus.OK);
        }

        ShipModel originalShip = shipModel.get();

        if (name != null) {
            if (name.isEmpty() || name.length() > 50) {
                return new ResponseEntity<>(updatedShip, HttpStatus.BAD_REQUEST);
            }
            originalShip.setName(name);
        }

        if (planet != null) {
            if (planet.isEmpty() || planet.length() > 50) {
                return new ResponseEntity<>(updatedShip, HttpStatus.BAD_REQUEST);
            }
            originalShip.setPlanet(updatedShip.getPlanet());
        }
        if (updatedShip.getShipType() != null) {
            originalShip.setShipType(updatedShip.getShipType());
        }

        if (prodDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(prodDate.getTime());
            if (calendar.get(Calendar.YEAR) < 2800 || calendar.get(Calendar.YEAR) > 3019) {
                return new ResponseEntity<>(updatedShip, HttpStatus.BAD_REQUEST);
            }
            Date date = calendar.getTime();
            originalShip.setProdDate(date);
        }
        Boolean isUsed = updatedShip.getUsed();
        if (isUsed != null) {
            originalShip.setUsed(isUsed);
        }
        if (speed != null) {
            if (speed < 0.01 || speed > 0.99) {
                return new ResponseEntity<>(updatedShip, HttpStatus.BAD_REQUEST);
            }
            originalShip.setSpeed(speed);
        }
        if (crewSize != null) {
            if (crewSize < 1 || crewSize > 9999) {
                return new ResponseEntity<>(updatedShip, HttpStatus.BAD_REQUEST);
            }
            originalShip.setCrewSize(crewSize);
        }

        double k = originalShip.getUsed() ? 0.5 : 1;
        int currentYear = 3019;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(originalShip.getProdDate().getTime());
        int shipProdYear = calendar.get(Calendar.YEAR);
        double rating = (80.0 * originalShip.getSpeed() * k) / (currentYear - shipProdYear + 1);
        originalShip.setRating(new BigDecimal(String.valueOf(rating)).setScale(2, RoundingMode.HALF_UP).doubleValue());
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
