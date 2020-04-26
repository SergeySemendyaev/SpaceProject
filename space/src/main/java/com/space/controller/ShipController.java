package com.space.controller;

import com.space.model.ShipModel;
import com.space.repository.ShipRepository;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/rest")
public class ShipController {
    @Autowired
    ShipRepository shipRepository;

    @GetMapping(path = "/ships")
    @ResponseBody
    List<ShipModel> getShips(@RequestParam(required = false) String name,
                             @RequestParam(required = false) String planet,
                             @RequestParam(required = false) Integer after,
                             @RequestParam(required = false) Integer before,
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
        if (order != null) {
            String property = (order.equals("DATE") ? "prodDate" : order.toLowerCase());
            Sort sort = new Sort(Sort.Direction.ASC, property);
            sort.getOrderFor(property);
            return shipRepository.findAll(sort);
        }
        return shipRepository.findAll();
    }

    @GetMapping(path = "/ships/count")
    Long getCount() {
        return shipRepository.count();
    }
}
