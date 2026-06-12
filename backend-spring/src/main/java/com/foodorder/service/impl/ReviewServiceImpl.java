package com.foodorder.service.impl;

import com.foodorder.dao.IReviewDAO;
import com.foodorder.dto.ReviewCreateRequest;
import com.foodorder.model.Review;
import com.foodorder.service.ReviewService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final IReviewDAO reviewDAO;

    public ReviewServiceImpl(IReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewDAO.findAllWithNames();
    }

    @Override
    @Transactional
    public Review createReview(ReviewCreateRequest request) {
        if (request.rating() < 1 || request.rating() > 5) {
            throw new IllegalArgumentException("Rating must be from 1 to 5.");
        }
        Review review = new Review();
        review.setUserId(request.userId());
        review.setFoodId(request.foodId());
        review.setRating(request.rating());
        review.setComment(request.comment());
        review.setReviewDate(LocalDateTime.now());
        return reviewDAO.save(review);
    }
}
