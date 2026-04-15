package com.pcabrera.hotel_search.infrastructure.adapter.out.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "search")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SearchDbModel {
    @Id
    private String searchId;

    private String hotelId;
    private LocalDate checkIn;
    private LocalDate checkOut;

    @ElementCollection
    @CollectionTable(name = "search_ages", joinColumns = @JoinColumn(name = "search_id"))
    @Column(name = "age")
    private List<Integer> ages;

    @Column(name = "ages_signature")
    private String agesSignature;
}
