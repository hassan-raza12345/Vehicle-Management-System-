package com.example.Security.Service;

import com.example.Security.Model.Role;
import com.example.Security.Model.User;
import com.example.Security.Model.VehicleListing;
import com.example.Security.Model.VehicleStatus;
import com.example.Security.Repository.UserRepository;
import com.example.Security.Repository.VehicleListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleListingService {

    private final VehicleListingRepository listingRepository;
    private final UserRepository userRepository;

    public VehicleListing createListing(VehicleListing listing, String userEmail) {
        User seller = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (seller.getRole() != Role.SELLER) {
            throw new RuntimeException("Only sellers can create vehicle listings.");
        }

        listing.setSeller(seller);
        return listingRepository.save(listing);
    }


    public List<VehicleListing> getAllListings() {
        return listingRepository.findAll();
    }
    public List<VehicleListing> getforSaleVehicle() {
        return listingRepository.findByStatus(VehicleStatus.FOR_SALE);
    }


    public VehicleListing getListingById(Long id) {
        return listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));
    }

    public List<VehicleListing> filterListings(String make, String model, Double minPrice, Double maxPrice) {
        if (make != null) return listingRepository.findByMakeContainingIgnoreCase(make);
        if (model != null) return listingRepository.findByModelContainingIgnoreCase(model);
        if (minPrice != null && maxPrice != null) return listingRepository.findByPriceBetween(minPrice, maxPrice);
        return listingRepository.findAll();
    }
    public VehicleListing updateVehicleStatus(Long vehicleId, VehicleStatus status, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        VehicleListing vehicle = listingRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        if (!user.getRole().equals(Role.SELLER)) {
            throw new RuntimeException("Only users with the SELLER role can update vehicle status.");
        }
        if (!vehicle.getSeller().equals(user)) {
            throw new RuntimeException("You are not authorized to update this vehicle.");
        }
        if (status != VehicleStatus.FOR_SALE && status != VehicleStatus.SOLD) {
            throw new IllegalArgumentException("Invalid status. Allowed statuses: FOR_SALE, SOLD.");
        }

        vehicle.setStatus(status);
        return listingRepository.save(vehicle);
    }
    public void deleteVehicleById(Long id) {
        if (!listingRepository.existsById(id)) {
            throw new RuntimeException("Vehicle not found with ID: " + id);
        }
        listingRepository.deleteById(id);
    }

}

