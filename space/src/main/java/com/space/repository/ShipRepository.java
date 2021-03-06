package com.space.repository;

import com.space.model.ShipModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipRepository extends JpaRepository<ShipModel, Long>, JpaSpecificationExecutor<ShipModel> {

}
