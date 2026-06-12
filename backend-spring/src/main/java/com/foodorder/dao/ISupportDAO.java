package com.foodorder.dao;

import com.foodorder.model.SupportRequest;
import java.util.List;

public interface ISupportDAO extends IGenericDAO<SupportRequest, Integer> {
    List<SupportRequest> findAllWithUsernames();
}
