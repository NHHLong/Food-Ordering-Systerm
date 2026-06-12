package com.foodorder.service;

import com.foodorder.dto.ReviewCreateRequest;
import com.foodorder.model.Review;
import java.util.List;

public interface ReviewService {
    List<Review> getAllReviews();
    Review createReview(ReviewCreateRequest request);
}
