package com.example.Security.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewID;

    private int rating;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "reviewerID", nullable = false)
    @JsonIgnore
    private User reviewer;

    @ManyToOne
    @JoinColumn(name = "revieweeID", nullable = false)
    @JsonIgnore
    private User reviewee;

    @ManyToOne
    @JoinColumn(name = "purchase_request_id", nullable = false)
    @JsonIgnore
    private PurchaseRequest purchaseRequest;

    public void setRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.rating = rating;
    }
}