package com.space.controller;

import com.space.model.Ship;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ShipController {

    private final ShipService shipService;

    @Autowired
    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    @PostMapping(value = "/ships")
    public ResponseEntity<?> create(@RequestBody Ship ship) {
        shipService.create(ship);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/ships")
    public ResponseEntity<List<Ship>> read() {
        final List<Ship> ships = shipService.readAll();

        return ships != null && !ships.isEmpty()
                ? new ResponseEntity<>(ships, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/ships/{id}")
    public ResponseEntity<Ship> read(@PathVariable(name = "id") int id) {
        final Ship ship = shipService.read(id);

        return ship != null
                ? new ResponseEntity<>(ship, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
