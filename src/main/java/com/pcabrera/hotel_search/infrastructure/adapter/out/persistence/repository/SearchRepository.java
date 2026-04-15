package com.pcabrera.hotel_search.infrastructure.adapter.out.persistence.repository;

import com.pcabrera.hotel_search.infrastructure.adapter.out.persistence.model.SearchDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface SearchRepository extends JpaRepository<SearchDbModel, String> {
    @Query("""
        select count(s)
        from SearchDbModel s
        where s.hotelId = :hotelId
          and s.checkIn = :checkIn
          and s.checkOut = :checkOut
          and s.agesSignature = :agesSignature
    """)
    long countEqualsSearch(
            @Param("hotelId") String hotelId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            @Param("agesSignature") String agesSignature
    );
}