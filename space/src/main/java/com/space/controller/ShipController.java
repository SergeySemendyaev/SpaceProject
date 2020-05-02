package com.space.controller;

import com.space.model.ShipModel;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    List<ShipModel> getShips(@RequestParam(required = false) String name,
                             @RequestParam(required = false) String planet,
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
        Page<ShipModel> page = null;
        /*
        if (name != null) {
            page = shipRepository.getAllByNameContains(name, PageRequest.of(0, 3, Sort.Direction.ASC, property));
        }
        else {
            page = shipRepository.findAll(pageable);
        }

        return page.getContent();

         */
        return shipService.getShips(name, planet, after, before,
                isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize,
                minRating, maxRating, property, pageable);
    }

    @GetMapping(path = "/ships/count")
    Long getCount() {
        return shipService.count();
    }
}
