package com.example.sushiorder.repository;

import com.example.sushiorder.model.Sushi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SushiRepository extends JpaRepository<Sushi, Integer> {

    @Query(value = "select * from sushi s where s.name =:name", nativeQuery = true)
    public Sushi findByName(String name);
}
