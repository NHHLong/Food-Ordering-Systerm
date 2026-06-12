package com.foodorder.dao;

import com.foodorder.model.Voucher;

public interface IVoucherDAO extends IGenericDAO<Voucher, Integer> {
    Voucher findByCode(String code);
    boolean existsByCode(String code);
    void markUsed(int voucherId);
}
