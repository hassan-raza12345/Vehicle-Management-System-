package com.example.Security.Controller;

import com.example.Security.Model.Review;
import com.example.Security.Model.User;
import com.example.Security.Service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    @GetMapping("/{id}/seller-rating")
    public ResponseEntity<?> getSellerRating(@PathVariable Long id) {
        try {

            double sellerRating = reviewService.getAverageRatingAsSeller(id);

            if (sellerRating == 0.0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No seller ratings found for user ID: " + id);
            }

            return ResponseEntity.ok(sellerRating);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching the seller rating: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/buyer-rating")
    public ResponseEntity<?> getBuyerRating(@PathVariable Long id) {
        try {
            System.out.println("Fetching buyer rating for user ID: " + id);
            double buyerRating = reviewService.getAverageRatingAsBuyer(id);

            if (buyerRating == 0.0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No buyer ratings found for user ID: " + id);
            }

            return ResponseEntity.ok(buyerRating);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching the buyer rating: " + e.getMessage());
        }
    }
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId, @AuthenticationPrincipal User user) {
        reviewService.deleteReview(reviewId, user);
        return ResponseEntity.ok("Review deleted successfully.");
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable Long reviewId,
            @RequestBody Review review,
            @AuthenticationPrincipal User user) {
        reviewService.updateReview(reviewId, review, user);
        return ResponseEntity.ok("Review updated successfully.");
    }
}
