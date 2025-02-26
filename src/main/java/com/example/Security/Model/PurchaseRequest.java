package com.example.Security.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@ToString(exclude = {"vehicle", "buyer"})
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
    @JsonIgnore

    private VehicleListing vehicle;

    @ManyToOne
    @JoinColumn(name = "buyer_id",  nullable = false)
    @JsonIgnore

    private User  buyer;
    @OneToMany(mappedBy = "purchaseRequest", cascade = CascadeType.ALL)
    private List<Review> reviews;
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;
}
