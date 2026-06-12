package com.foodorder.dao;

import com.foodorder.model.Review;
import java.util.List;

public interface IReviewDAO extends IGenericDAO<Review, Integer> {
    List<Review> findAllWithNames();
}
