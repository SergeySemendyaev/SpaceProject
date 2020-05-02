package com.space.service;

import com.space.model.ShipModel;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.List;

@Service
public class ShipService {
    private final ShipRepository shipRepository;

    @Autowired
    public ShipService(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    public Long count() {
        return shipRepository.count();
    }

    public List<ShipModel> getShips(String name, String planet,
                                    Long after, Long before,
                                    Boolean isUsed,
                                    Double minSpeed, Double maxSpeed,
                                    Integer minCrewSize, Integer maxCrewSize,
                                    Double minRating, Double maxRating, String property, Pageable pageable) {
        return shipRepository.findAll((Specification<ShipModel>) (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (name != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }
            if (planet != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("planet"), "%" + planet + "%"));
            }
            if (after != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("prodDate"), new Date(after)));
            }
            if (before != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("prodDate"), new Date(before)));
            }
            if (isUsed != null) {
                if (isUsed) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.isTrue(root.get("isUsed")));
                }
                else {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.isFalse(root.get("isUsed")));
                }
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
}
