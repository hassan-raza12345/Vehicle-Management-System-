package com.example.Security.Service;

import com.example.Security.Exception.NoReviewsFoundException;
import com.example.Security.Exception.RatingCalculationException;
import com.example.Security.Model.PurchaseRequest;
import com.example.Security.Model.RequestStatus;
import com.example.Security.Model.Review;
import com.example.Security.Model.User;
import com.example.Security.Repository.PurchaseRequestRepository;
import com.example.Security.Repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final PurchaseRequestRepository purchaseRequestRepository;
    public Review createReview(Long purchaseRequestId, User reviewer, int rating, String comment) {

        PurchaseRequest request = purchaseRequestRepository.findById(purchaseRequestId)
                .orElseThrow(() -> new RuntimeException("Purchase request not found"));

        if (!RequestStatus.APPROVED.equals(request.getStatus())) {
            throw new RuntimeException("You can only review an approved purchase request.");
        }

        User seller = request.getVehicle().getOwner();
        User buyer = request.getBuyer();
        String reviewerRole = reviewer.getId().equals(buyer.getId()) ? "Buyer" : "Seller";
        System.out.println("Reviewer is acting as: " + reviewerRole);

        User reviewee = reviewer.getId().equals(buyer.getId()) ? seller : buyer;

        if (!reviewer.getId().equals(buyer.getId()) && !reviewer.getId().equals(seller.getId())) {
            throw new RuntimeException("You are not authorized to review this transaction.");
        }

        boolean reviewExists = reviewRepository.existsByReviewerIdAndPurchaseRequestRequestID(reviewer.getId(), purchaseRequestId);
        if (reviewExists) {
            throw new RuntimeException("You have already reviewed this purchase request.");
        }

        Review review = new Review();
        review.setPurchaseRequest(request);
        review.setReviewer(reviewer);
        review.setReviewee(reviewee);
        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    public double getAverageRatingAsBuyer(Long userId) {
        try {
            List<Review> buyerReviews = reviewRepository.findReviewsAsBuyer(userId);

            if (buyerReviews == null || buyerReviews.isEmpty()) {
                return 0.0;
            }

            return calculateAverageRating(buyerReviews);
        } catch (Exception e) {
            throw new RatingCalculationException("Error while fetching buyer rating for user ID: " + userId, e);
        }
    }


    public double getAverageRatingAsSeller(Long userId) {
        try {
            List<Review> sellerReviews = reviewRepository.findReviewsAsSeller(userId);

            if (sellerReviews == null || sellerReviews.isEmpty()) {
                return 0.0;
            }

            return calculateAverageRating(sellerReviews);
        } catch (Exception e) {
            throw new RatingCalculationException("Error while fetching seller rating for user ID: " + userId, e);
        }
    }


    private double calculateAverageRating(List<Review> reviews) {
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }
    public void deleteReview(Long reviewId, User user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        if (!review.getReviewer().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to delete this review.");
        }

        reviewRepository.delete(review);
    }
    public void updateReview(Long reviewId, Review updatedReview, User user) {
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        if (!existingReview.getReviewer().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to update this review.");
        }
        existingReview.setRating(updatedReview.getRating());
        existingReview.setComment(updatedReview.getComment());
        reviewRepository.save(existingReview);
    }

}
