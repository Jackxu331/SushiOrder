package com.example.repository;
import com.example.model.SushiOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SushiOrderRepository extends CrudRepository<SushiOrder, Integer> {

}
