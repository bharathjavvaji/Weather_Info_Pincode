package com.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weather.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
	
}
