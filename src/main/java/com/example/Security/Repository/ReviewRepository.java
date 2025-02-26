package com.example.Security.Repository;
import com.example.Security.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.purchaseRequest.buyer.id = :userId")
    List<Review> findReviewsAsBuyer(@Param("userId") Long userId);


    @Query("SELECT r FROM Review r WHERE r.reviewee.id = :userId AND r.purchaseRequest.vehicle.owner.id = :userId")
    List<Review> findReviewsAsSeller(@Param("userId") Long userId);

    boolean existsByReviewerIdAndPurchaseRequestRequestID(Long reviewerId, Long purchaseRequestId);

}
