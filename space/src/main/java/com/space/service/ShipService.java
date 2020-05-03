package com.space.service;

import com.space.model.ShipModel;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.Predicate;
import java.util.*;

@Service
public class ShipService {
    private final ShipRepository shipRepository;

    @Autowired
    public ShipService(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    public Long count(
            String name,
            String planet,
            ShipType shipType,
            Long after, Long before,
            Boolean isUsed,
            Double minSpeed, Double maxSpeed,
            Integer minCrewSize, Integer maxCrewSize,
            Double minRating, Double maxRating
    ) {

        return shipRepository.count((Specification<ShipModel>) (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (name != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }
            if (planet != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("planet"), "%" + planet + "%"));
            }
            if (after != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(after);
                calendar.set(Calendar.MONTH, Calendar.JANUARY);
                calendar.set(Calendar.DATE, 1);
                calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.AM_PM, Calendar.AM);
                Date startDate = calendar.getTime();
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("prodDate"), startDate));
            }
            if (before != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(before);
                calendar.set(Calendar.MONTH, Calendar.DECEMBER);
                calendar.set(Calendar.DATE, 31);
                calendar.set(Calendar.HOUR, 11);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 999);
                calendar.set(Calendar.AM_PM, Calendar.PM);
                Date endDate = calendar.getTime();
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("prodDate"), endDate));
            }
            if (shipType != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("shipType"), shipType));
            }
            if (isUsed != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isUsed"), isUsed));
            }
            if (minSpeed != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("speed"), minSpeed));
            }
            if (maxSpeed != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("speed"), maxSpeed));
            }
            if (minCrewSize != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("crewSize"), minCrewSize));
            }
            if (maxCrewSize != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("crewSize"), maxCrewSize));
            }
            if (minRating != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), minRating));
            }
            if (maxRating != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("rating"), maxRating));
            }
            return predicate;
        });
    }

    public List<ShipModel> getShips(
            String name,
            String planet,
            ShipType shipType,
            Long after, Long before,
            Boolean isUsed,
            Double minSpeed, Double maxSpeed,
            Integer minCrewSize, Integer maxCrewSize,
            Double minRating, Double maxRating,
            String property,
            Pageable pageable
    ) {

        return shipRepository.findAll((Specification<ShipModel>) (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (name != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }
            if (planet != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("planet"), "%" + planet + "%"));
            }
            if (after != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(after);
                calendar.set(Calendar.MONTH, Calendar.JANUARY);
                calendar.set(Calendar.DATE, 1);
                calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.AM_PM, Calendar.AM);
                Date startDate = calendar.getTime();
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("prodDate"), startDate));
            }
            if (before != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(before);
                calendar.set(Calendar.MONTH, Calendar.DECEMBER);
                calendar.set(Calendar.DATE, 31);
                calendar.set(Calendar.HOUR, 11);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 999);
                calendar.set(Calendar.AM_PM, Calendar.PM);
                Date endDate = calendar.getTime();
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("prodDate"), endDate));
            }
            if (shipType != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("shipType"), shipType));
            }
            if (isUsed != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isUsed"), isUsed));
            }
            if (minSpeed != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("speed"), minSpeed));
            }
            if (maxSpeed != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("speed"), maxSpeed));
            }
            if (minCrewSize != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("crewSize"), minCrewSize));
            }
            if (maxCrewSize != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("crewSize"), maxCrewSize));
            }
            if (minRating != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), minRating));
            }
            if (maxRating != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("rating"), maxRating));
            }
            query.orderBy(criteriaBuilder.asc(root.get(property)));
            return predicate;
        }, pageable).getContent();
    }

    public ShipModel createShip(ShipModel shipModel) {
        shipModel = shipRepository.save(shipModel);
        return shipModel;
    }

    public Optional<ShipModel> getShipById(Long id) {
        return shipRepository.findById(id);
    }

    public ShipModel updateShip(ShipModel shipToUpdate) {
        return shipRepository.save(shipToUpdate);
    }

    public void deleteShip(Long id) {
        shipRepository.deleteById(id);
    }
}
