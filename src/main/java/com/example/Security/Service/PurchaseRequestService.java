package com.example.Security.Service;

import com.example.Security.Model.*;
import com.example.Security.Repository.PurchaseRequestRepository;
import com.example.Security.Repository.UserRepository;
import com.example.Security.Repository.VehicleListingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseRequestService {

    private final PurchaseRequestRepository purchaseRequestRepository;
    private final UserRepository userRepository;
    private final VehicleListingRepository vehicleListingRepository;

    public PurchaseRequestService(PurchaseRequestRepository purchaseRequestRepository,
                                  UserRepository userRepository,
                                  VehicleListingRepository vehicleListingRepository) {
        this.purchaseRequestRepository = purchaseRequestRepository;
        this.userRepository = userRepository;
        this.vehicleListingRepository = vehicleListingRepository;
    }

    public PurchaseRequest createPurchaseRequest(Long vehicleId, String buyerEmail) {
        User buyer = userRepository.findByEmail(buyerEmail)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));

        VehicleListing vehicle = vehicleListingRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        if (!vehicle.getStatus().equals(VehicleStatus.FOR_SALE)) {
            throw new RuntimeException("Vehicle is not for sale.");
        }
        if (!buyer.getRole().equals(Role.BUYER)) {
            throw new RuntimeException("Only users with the BUYER role can make a purchase.");
        }

        PurchaseRequest request = new PurchaseRequest();
        request.setBuyer(buyer);
        request.setVehicle(vehicle);

        return purchaseRequestRepository.save(request);
    }

    public List<PurchaseRequest> getRequestsByBuyer(String buyerEmail) {
        User buyer = userRepository.findByEmail(buyerEmail)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));
        return purchaseRequestRepository.findByBuyer(buyer);
    }

    public List<PurchaseRequest> getRequestsByVehicle(Long vehicleId) {
        VehicleListing vehicle = vehicleListingRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        return purchaseRequestRepository.findByVehicle(vehicle);
    }

    public PurchaseRequest updateRequestStatus(Long requestId, RequestStatus status) {
        PurchaseRequest request = purchaseRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus(status);
        return purchaseRequestRepository.save(request);
    }
    public PurchaseRequest handlePurchaseRequest(Long requestId, String sellerEmail, RequestStatus decision) {
        User seller = userRepository.findByEmail(sellerEmail)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        PurchaseRequest request = purchaseRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Purchase request not found"));

        VehicleListing vehicle = request.getVehicle();

        if (!vehicle.getSeller().equals(seller)) {
            throw new RuntimeException("You are not authorized to handle this request.");
        }

        if (!vehicle.getStatus().equals(VehicleStatus.FOR_SALE)) {
            throw new RuntimeException("Cannot process requests for vehicles not for sale or sold.");
        }

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("This request has already been handled.");
        }

        request.setStatus(decision);

        if (decision == RequestStatus.ACCEPTED) {
            vehicle.setStatus(VehicleStatus.SOLD);  // Mark vehicle as sold
            vehicleListingRepository.save(vehicle);
            // Optional: Notify buyer and seller (email, notification service, etc.)
        }

        return purchaseRequestRepository.save(request);
    }

}
