package com.foodorder.dao;

import com.foodorder.model.SupportRequest;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class SupportDAOImpl extends GenericDAOImpl<SupportRequest, Integer> implements ISupportDAO {
    public SupportDAOImpl() {
        super(SupportRequest.class);
    }

    @Override
    public List<SupportRequest> findAllWithUsernames() {
        List<SupportRequest> requests = entityManager
            .createQuery("SELECT s FROM SupportRequest s ORDER BY s.createdAt DESC", SupportRequest.class)
            .getResultList();
        for (SupportRequest request : requests) {
            request.setUsername(entityManager
                .createQuery("SELECT u.username FROM UserEntity u WHERE u.userId = :userId", String.class)
                .setParameter("userId", request.getUserId())
                .getResultStream()
                .findFirst()
                .orElse(""));
        }
        return requests;
    }
}
