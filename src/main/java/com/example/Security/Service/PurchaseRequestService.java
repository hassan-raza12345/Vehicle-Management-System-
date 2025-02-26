package com.example.Security.Service;

import com.example.Security.Exception.UnauthorizedAccessException;
import com.example.Security.Model.*;
import com.example.Security.Repository.PurchaseRequestRepository;
import com.example.Security.Repository.ReviewRepository;
import com.example.Security.Repository.UserRepository;
import com.example.Security.Repository.VehicleListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

import static com.example.Security.Model.Role.SELLER;

@Service
@RequiredArgsConstructor
public class PurchaseRequestService {

    private final PurchaseRequestRepository purchaseRequestRepository;
    private final UserRepository userRepository;
    private final VehicleListingRepository vehicleListingRepository;
    private final ReviewRepository reviewRepository;


    public PurchaseRequest createPurchaseRequest(Long userID, Long vehicleID) {
        VehicleListing vehicle = vehicleListingRepository.findById(vehicleID)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        if (vehicle.getStatus() != VehicleStatus.FOR_SALE) {
            throw new RuntimeException("This vehicle is not available for sale.");
        }

        User buyer = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (vehicle.getOwner().getId().equals(userID)) {
            throw new RuntimeException("You cannot request to purchase your own car.");
        }

        boolean requestExists = purchaseRequestRepository.existsByBuyerAndVehicle(buyer, vehicle);
        if (requestExists) {
            throw new RuntimeException("You have already requested to purchase this vehicle.");
        }

        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setBuyer(buyer);
        purchaseRequest.setVehicle(vehicle);
        purchaseRequest.setStatus(RequestStatus.PENDING);

        return purchaseRequestRepository.save(purchaseRequest);
    }

    public void     processPurchaseRequestDecision(Long requestID, RequestStatus status, Long sellerId) {
        PurchaseRequest request = purchaseRequestRepository.findById(requestID)
                .orElseThrow(() -> new RuntimeException("Purchase request not found"));


        if (!request.getVehicle().getOwner().getId().equals(sellerId)) {
            throw new RuntimeException("You are not authorized to make this decision.");
        }

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("This request has already been processed.");
        }

        request.setStatus(status);
        purchaseRequestRepository.save(request);

        if (status == RequestStatus.APPROVED)
        {
            VehicleListing vehicle = request.getVehicle();
            vehicle.setStatus(VehicleStatus.SOLD);
            vehicleListingRepository.save(vehicle);
        }
    }

    public PurchaseRequest updateRequestStatus(Long requestId, RequestStatus status) {
        PurchaseRequest request = purchaseRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus(status);
        return purchaseRequestRepository.save(request);
    }

    public List<PurchaseRequest> getAllPurchaseRequests() {
        return purchaseRequestRepository.findAll();
    }


    public PurchaseRequest getPurchaseRequestById(Long id) {
        return purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase request not found"));
    }

    public void deletePurchaseRequest(Long id) {
        purchaseRequestRepository.deleteById(id);

    }



    public List<Review> getReviewsForPurchaseRequest(Long purchaseRequestID) {
        PurchaseRequest purchaseRequest = purchaseRequestRepository.findById(purchaseRequestID)
                .orElseThrow(() -> new RuntimeException("Purchase request not found"));
        return purchaseRequest.getReviews();
    }


}