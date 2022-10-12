package com.example.sushiorder.repository;

import com.example.sushiorder.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {

    @Query(value = "select * from status s where s.name =:name", nativeQuery = true)
    Status findByName(String name);

    @Query(value = "select * from status s where s.id =: id", nativeQuery = true)
    Status findById(int id);
}
