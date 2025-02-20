package com.example.Security.Controller;

import com.example.Security.Model.PurchaseRequest;
import com.example.Security.Model.RequestStatus;
import com.example.Security.Service.PurchaseRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/purchase-requests")
public class PurchaseRequestController {

    private final PurchaseRequestService purchaseRequestService;

    public PurchaseRequestController(PurchaseRequestService purchaseRequestService) {
        this.purchaseRequestService = purchaseRequestService;
    }

    @PostMapping("/create/{vehicleId}")
    public ResponseEntity<PurchaseRequest> createRequest(@PathVariable Long vehicleId,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        PurchaseRequest request = purchaseRequestService.createPurchaseRequest(vehicleId, userDetails.getUsername());
        return ResponseEntity.ok(request);
    }

    @GetMapping("/buyer")
    public ResponseEntity<List<PurchaseRequest>> getBuyerRequests(@AuthenticationPrincipal UserDetails userDetails) {
        List<PurchaseRequest> requests = purchaseRequestService.getRequestsByBuyer(userDetails.getUsername());
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<PurchaseRequest>> getRequestsByVehicle(@PathVariable Long vehicleId) {
        List<PurchaseRequest> requests = purchaseRequestService.getRequestsByVehicle(vehicleId);
        return ResponseEntity.ok(requests);
    }

    @PutMapping("/update-status/{requestId}")
    public ResponseEntity<PurchaseRequest> updateRequestStatus(@PathVariable Long requestId,
                                                               @RequestParam RequestStatus status) {
        PurchaseRequest updatedRequest = purchaseRequestService.updateRequestStatus(requestId, status);
        return ResponseEntity.ok(updatedRequest);
    }
    @PutMapping("/{requestId}/decision")
    public ResponseEntity<PurchaseRequest> handleRequest(
            @PathVariable Long requestId,
            @RequestParam RequestStatus decision,
            Principal principal) {

        PurchaseRequest updatedRequest = purchaseRequestService.handlePurchaseRequest(requestId, principal.getName(), decision);
        return ResponseEntity.ok(updatedRequest);
    }
}