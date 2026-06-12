package com.foodorder.controller;

import com.foodorder.dto.ReviewCreateRequest;
import com.foodorder.model.Review;
import com.foodorder.service.ReviewService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviews;

    public ReviewController(ReviewService reviews) {
        this.reviews = reviews;
    }

    @GetMapping
    public List<Review> getAll() {
        return reviews.getAllReviews();
    }

    @PostMapping
    public Review add(@RequestBody ReviewCreateRequest request) {
        return reviews.createReview(request);
    }
}
