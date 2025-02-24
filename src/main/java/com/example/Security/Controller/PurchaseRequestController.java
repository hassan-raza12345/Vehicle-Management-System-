package com.example.Security.Controller;

import com.example.Security.Model.PurchaseRequest;
import com.example.Security.Model.RequestStatus;
import com.example.Security.Model.Review;
import com.example.Security.Service.PurchaseRequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/purchase-requests")
@AllArgsConstructor
public class PurchaseRequestController {

    private final PurchaseRequestService purchaseRequestService;
    @PostMapping
    public ResponseEntity<?> createPurchaseRequest(@RequestParam Long userID, @RequestParam Long vehicleID) {
        try {
            PurchaseRequest purchaseRequest = purchaseRequestService.createPurchaseRequest(userID, vehicleID);
            return ResponseEntity.ok().body("Purchase request created successfully. Request ID: " + purchaseRequest.getRequestID());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/decision")
    public ResponseEntity<?> processPurchaseRequestDecision(
            @RequestParam Long requestID,
            @RequestParam RequestStatus status) {
        try {


            purchaseRequestService.processPurchaseRequestDecision(requestID, status);
            String message = status == RequestStatus.APPROVED
                    ? "Purchase request approved successfully"
                    : "Purchase request rejected successfully";
            return ResponseEntity.ok().body(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid decision");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<List<PurchaseRequest>> getAllPurchaseRequests() {
        List<PurchaseRequest> purchaseRequests = purchaseRequestService.getAllPurchaseRequests();
        return ResponseEntity.ok(purchaseRequests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseRequest> getPurchaseRequestById(@PathVariable Long id) {
        PurchaseRequest purchaseRequest = purchaseRequestService.getPurchaseRequestById(id);
        return ResponseEntity.ok(purchaseRequest);
    }

    @PutMapping("/update-status/{requestId}")
    public ResponseEntity<PurchaseRequest> updateRequestStatus(@PathVariable Long requestId,
                                                               @RequestParam RequestStatus status) {
        PurchaseRequest updatedRequest = purchaseRequestService.updateRequestStatus(requestId, status);
        return ResponseEntity.ok(updatedRequest);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePurchaseRequest(@PathVariable Long id) {
        purchaseRequestService.deletePurchaseRequest(id);
        return ResponseEntity.ok().body("Purchase request deleted successfully");
    }
    @PostMapping("/{id}/reviews")
    public ResponseEntity<Review> addReview(
            @PathVariable Long id,
            @RequestParam Long reviewerID,
            @RequestParam Long revieweeID,
            @RequestParam int rating,
            @RequestParam(required = false) String comment) {
        Review review = purchaseRequestService.addReview(id, reviewerID, revieweeID, rating, comment);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<Review>> getReviewsForPurchaseRequest(@PathVariable Long id) {
        List<Review> reviews = purchaseRequestService.getReviewsForPurchaseRequest(id);
        return ResponseEntity.ok(reviews);
    }

}