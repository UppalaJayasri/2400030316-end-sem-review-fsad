package com.indianheritage.app.repository;

import com.indianheritage.app.entity.Place;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findAllByOrderByIdAsc();
    boolean existsByName(String name);
}
