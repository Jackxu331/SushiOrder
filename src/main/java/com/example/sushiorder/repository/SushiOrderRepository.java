package com.example.sushiorder.repository;

import com.example.sushiorder.model.SushiOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SushiOrderRepository extends JpaRepository<SushiOrder, Integer> {

}
