package com.example.Security.Controller;

import com.example.Security.Model.PurchaseRequest;
import com.example.Security.Model.RequestStatus;
import com.example.Security.Model.Review;
import com.example.Security.Model.User;
import com.example.Security.Repository.UserRepository;
import com.example.Security.Service.PurchaseRequestService;
import com.example.Security.Service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/purchase-requests")
@RequiredArgsConstructor
public class PurchaseRequestController {

    private final PurchaseRequestService purchaseRequestService;
    private final UserRepository userRepository;
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> createPurchaseRequest(
            @RequestParam Long vehicleID,
            @AuthenticationPrincipal UserDetails userDetails)
    {
        try {
            String username = userDetails.getUsername();
            User buyer = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            PurchaseRequest request = purchaseRequestService.createPurchaseRequest(buyer.getId(), vehicleID);
            return ResponseEntity.ok("Purchase request created successfully. Request ID: " + request.getRequestID());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/decision")
    public ResponseEntity<?> processPurchaseRequestDecision(
            @RequestParam Long requestID,
            @RequestParam RequestStatus status,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            User seller = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Seller not found"));

            purchaseRequestService.processPurchaseRequestDecision(requestID, status, seller.getId());

            String message = (status == RequestStatus.APPROVED)
                    ? "Purchase request approved successfully."
                    : "Purchase request rejected successfully.";

            return ResponseEntity.ok().body(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{vehicleId}/all")
    public ResponseEntity<?> getAllPurchaseRequests(
            @PathVariable Long vehicleId,
            @AuthenticationPrincipal User user) {
        try {
            List<PurchaseRequest> purchaseRequests = purchaseRequestService.getAllPurchaseRequests(vehicleId, user);

            if (purchaseRequests.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No purchase requests found for vehicle ID: " + vehicleId);
            }

            return ResponseEntity.ok(purchaseRequests);
        }
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching purchase requests: " + ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPurchaseRequestById(@PathVariable Long id) {
        try {
            PurchaseRequest purchaseRequest = purchaseRequestService.getPurchaseRequestById(id);

            return ResponseEntity.ok(purchaseRequest);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching the purchase request: " + ex.getMessage());
        }
    }

    @PutMapping("/update-status/{requestId}")
    public ResponseEntity<?> updateRequestStatus(@PathVariable Long requestId,
                                                 @RequestParam RequestStatus status) {
        try {
            PurchaseRequest updatedRequest = purchaseRequestService.updateRequestStatus(requestId, status);

            return ResponseEntity.ok(updatedRequest);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating purchase request status: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePurchaseRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        try {
            purchaseRequestService.deletePurchaseRequest(id, user);
            return ResponseEntity.ok("Purchase request deleted successfully.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the purchase request: " + ex.getMessage());
        }
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<?> createReview(
            @PathVariable("id") Long purchaseRequestId,
            @RequestParam int rating,
            @RequestParam(required = false) String comment,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (rating < 1 || rating > 5)
        {
            return ResponseEntity.badRequest().body("Rating must be between 1 and 5.");
        }

        User reviewer = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));

        reviewService.createReview(purchaseRequestId, reviewer, rating, comment);
        return ResponseEntity.ok("Review submitted successfully.");
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<Review>> getReviewsForPurchaseRequest(@PathVariable Long id) {
        List<Review> reviews = purchaseRequestService.getReviewsForPurchaseRequest(id);
        return ResponseEntity.ok(reviews);
    }

}