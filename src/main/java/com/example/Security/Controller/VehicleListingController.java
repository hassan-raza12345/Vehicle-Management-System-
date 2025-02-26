package com.example.Security.Controller;
import com.example.Security.Model.RequestStatus;
import com.example.Security.Model.User;
import com.example.Security.Model.VehicleListing;
import com.example.Security.Model.VehicleStatus;
import com.example.Security.Repository.UserRepository;
import com.example.Security.Service.PurchaseRequestService;
import com.example.Security.Service.ReviewService;
import com.example.Security.Service.UserService;
import com.example.Security.Service.VehicleListingService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
@RequiredArgsConstructor
public class VehicleListingController {

    private final VehicleListingService listingService;
 private final ReviewService reviewService;



    @PostMapping("/create")
    public ResponseEntity<VehicleListing> createListing(@RequestBody VehicleListing listing) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(listingService.createListing(listing, email));
    }







    @GetMapping
    public ResponseEntity<List<VehicleListing>> getAllListings() {
        return ResponseEntity.ok(listingService.getAllListings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleListing> getListingById(@PathVariable Long id) {
        return ResponseEntity.ok(listingService.getListingById(id));
    }
    @GetMapping("/forSale")
    public ResponseEntity<List<VehicleListing>> getForSaleVehicles() {
        return ResponseEntity.ok(listingService.getforSaleVehicle());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {

        listingService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{vehicleId}/changestatus")
    public ResponseEntity<VehicleListing> updateVehicleStatus(
            @PathVariable Long vehicleId,
            @RequestParam VehicleStatus status,
            Principal principal) {
        System.out.println(status);
        VehicleListing updatedVehicle = listingService.updateVehicleStatus(vehicleId, status, principal.getName());
        return ResponseEntity.ok(updatedVehicle);
    }



    @GetMapping("/search")
    public ResponseEntity<List<VehicleListing>> searchListings(
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        return ResponseEntity.ok(listingService.filterListings(make, model, minPrice, maxPrice));
    }

    // 📊 Get average rating for a user as a seller




}
