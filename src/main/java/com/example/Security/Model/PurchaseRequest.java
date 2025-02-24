package com.example.Security.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Service
@Data
public class PurchaseRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestID;
    private LocalDateTime requestDate;
    private LocalDateTime approvalDate;
    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private VehicleListing vehicle;

    @ManyToOne
    @JoinColumn(name = "user_id",  nullable = false)
    private User  user;
    @OneToMany(mappedBy = "purchaseRequest", cascade = CascadeType.ALL)
    private List<Review> reviews;
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;


}
