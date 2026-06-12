package com.foodorder.dao;

import com.foodorder.model.Review;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewDAOImpl extends GenericDAOImpl<Review, Integer> implements IReviewDAO {
    public ReviewDAOImpl() {
        super(Review.class);
    }

    @Override
    public List<Review> findAllWithNames() {
        List<Review> reviews = entityManager
            .createQuery("SELECT r FROM Review r ORDER BY r.reviewDate DESC", Review.class)
            .getResultList();
        for (Review review : reviews) {
            review.setUsername(entityManager
                .createQuery("SELECT u.username FROM UserEntity u WHERE u.userId = :userId", String.class)
                .setParameter("userId", review.getUserId())
                .getResultStream()
                .findFirst()
                .orElse(""));
            review.setFoodName(entityManager
                .createQuery("SELECT f.name FROM Food f WHERE f.foodId = :foodId", String.class)
                .setParameter("foodId", review.getFoodId())
                .getResultStream()
                .findFirst()
                .orElse(""));
        }
        return reviews;
    }
}
