package com.example.Security.Repository;

import com.example.Security.Model.PurchaseRequest;
import com.example.Security.Model.User;
import com.example.Security.Model.VehicleListing;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {
    boolean existsByBuyerAndVehicle(User Buyer, VehicleListing vehicle);

}