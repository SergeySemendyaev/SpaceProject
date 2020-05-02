package com.space.repository;

import com.space.model.ShipModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipRepository extends JpaRepository<ShipModel, Long>, JpaSpecificationExecutor {
    @Override
    Page findAll(Specification spec, Pageable pageable);
}
