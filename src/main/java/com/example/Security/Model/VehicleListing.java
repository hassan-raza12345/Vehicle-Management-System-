package com.example.Security.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class VehicleListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String make;
    private String model;
    private int year;
    private double price;

    @Column(length = 1000)
    private String description;
    @Enumerated(EnumType.STRING)
    private VehicleStatus status ;
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<PurchaseRequest> purchaseRequests;


}
