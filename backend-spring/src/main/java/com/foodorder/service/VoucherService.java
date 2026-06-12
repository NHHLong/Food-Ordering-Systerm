package com.foodorder.service;

import com.foodorder.dto.VoucherSaveRequest;
import com.foodorder.model.Voucher;
import java.math.BigDecimal;
import java.util.List;

public interface VoucherService {
    Voucher applyVoucher(String code, BigDecimal orderTotal);
    List<Voucher> getAllVouchers();
    Voucher saveVoucher(VoucherSaveRequest request);
    void markVoucherUsed(int voucherId);
}
