package com.example.Security.Repository;

import com.example.Security.Model.User;
import com.example.Security.Model.VehicleListing;
import com.example.Security.Model.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleListingRepository extends JpaRepository<VehicleListing, Long> {

    List<VehicleListing> findByMakeContainingIgnoreCase(String make);
    List<VehicleListing> findByModelContainingIgnoreCase(String model);
    List<VehicleListing> findByPriceBetween(double minPrice, double maxPrice);
    List<VehicleListing> findByStatus(VehicleStatus status);
}