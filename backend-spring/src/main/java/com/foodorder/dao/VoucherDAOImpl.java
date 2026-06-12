package com.foodorder.dao;

import com.foodorder.model.Voucher;
import java.util.HashMap;
import org.springframework.stereotype.Repository;

@Repository
public class VoucherDAOImpl extends GenericDAOImpl<Voucher, Integer> implements IVoucherDAO {
    public VoucherDAOImpl() {
        super(Voucher.class);
    }

    @Override
    public Voucher findByCode(String code) {
        String jpql = "SELECT v FROM Voucher v WHERE v.code = :code";
        var params = new HashMap<String, Object>();
        params.put("code", code);
        return super.findSingleByJPQL(jpql, params);
    }

    @Override
    public boolean existsByCode(String code) {
        String jpql = "SELECT COUNT(v) FROM Voucher v WHERE v.code = :code";
        Long count = entityManager.createQuery(jpql, Long.class)
            .setParameter("code", code)
            .getSingleResult();
        return count != null && count > 0;
    }

    @Override
    public void markUsed(int voucherId) {
        findById(voucherId).ifPresent(voucher -> {
            voucher.setUsedCount(voucher.getUsedCount() + 1);
            update(voucher);
        });
    }
}
