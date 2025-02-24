package com.example.Security.Service;

import com.example.Security.Exception.UnauthorizedAccessException;
import com.example.Security.Model.*;
import com.example.Security.Repository.PurchaseRequestRepository;
import com.example.Security.Repository.ReviewRepository;
import com.example.Security.Repository.UserRepository;
import com.example.Security.Repository.VehicleListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("User not found"));

        VehicleListing vehicle = vehicleListingRepository.findById(vehicleID)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        if (vehicle.getStatus() != VehicleStatus.FOR_SALE) {
            throw new RuntimeException("Vehicle is not available for sale");
        }


        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setUser(user);
        purchaseRequest.setVehicle(vehicle);
        purchaseRequest.setStatus(RequestStatus.PENDING);
        purchaseRequest.setRequestDate(java.time.LocalDateTime.now());


        vehicle.setStatus(VehicleStatus.PENDING);
        vehicleListingRepository.save(vehicle);

        return purchaseRequestRepository.save(purchaseRequest);
    }

    public void processPurchaseRequestDecision(Long requestID, RequestStatus decision) {

        PurchaseRequest purchaseRequest = purchaseRequestRepository.findById(requestID)
                .orElseThrow(() -> new RuntimeException("Purchase request not found"));

        if (decision != RequestStatus.APPROVED && decision != RequestStatus.REJECTED) {
            throw new RuntimeException("Invalid decision");
        }

        purchaseRequest.setStatus(decision);
        purchaseRequest.setApprovalDate(java.time.LocalDateTime.now());
        purchaseRequestRepository.save(purchaseRequest);

        VehicleListing vehicle = purchaseRequest.getVehicle();
        if (decision == RequestStatus.APPROVED) {
            vehicle.setStatus(VehicleStatus.SOLD);
        } else if (decision == RequestStatus.REJECTED) {
            vehicle.setStatus(VehicleStatus.FOR_SALE);
        }
        vehicleListingRepository.save(vehicle);
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

    public Review addReview(Long purchaseRequestID, Long reviewerID, Long revieweeID, int rating, String comment) {
        PurchaseRequest purchaseRequest = purchaseRequestRepository.findById(purchaseRequestID)
                .orElseThrow(() -> new RuntimeException("Purchase request not found"));
        User reviewer = userRepository.findById(reviewerID)
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));
        User reviewee = userRepository.findById(revieweeID)
                .orElseThrow(() -> new RuntimeException("Reviewee not found"));
        Review review = new Review();
        review.setRating(rating);
        review.setComment(comment);
        review.setReviewer(reviewer);
        review.setReviewee(reviewee);
        review.setPurchaseRequest(purchaseRequest);

        return reviewRepository.save(review);
    }

    public List<Review> getReviewsForPurchaseRequest(Long purchaseRequestID) {
        PurchaseRequest purchaseRequest = purchaseRequestRepository.findById(purchaseRequestID)
                .orElseThrow(() -> new RuntimeException("Purchase request not found"));
        return purchaseRequest.getReviews();
    }
}