package com.foodorder.service.impl;

import com.foodorder.dao.IVoucherDAO;
import com.foodorder.dto.VoucherSaveRequest;
import com.foodorder.model.Voucher;
import com.foodorder.service.VoucherService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoucherServiceImpl implements VoucherService {
    private final IVoucherDAO voucherDAO;

    public VoucherServiceImpl(IVoucherDAO voucherDAO) {
        this.voucherDAO = voucherDAO;
    }

    @Override
    public Voucher applyVoucher(String code, BigDecimal orderTotal) {
        Voucher voucher = voucherDAO.findByCode(code == null ? "" : code.trim());
        if (voucher == null) throw new IllegalArgumentException("Voucher does not exist.");
        if (!voucher.isActive()) throw new IllegalArgumentException("Voucher is inactive.");
        LocalDateTime now = LocalDateTime.now();
        if ((voucher.getStartDate() != null && voucher.getStartDate().isAfter(now))
            || (voucher.getExpiryDate() != null && voucher.getExpiryDate().isBefore(now))) {
            throw new IllegalArgumentException("Voucher is expired or not started.");
        }
        if (orderTotal.compareTo(BigDecimal.valueOf(voucher.getMinimumOrderValue())) < 0) {
            throw new IllegalArgumentException("Order total does not meet voucher minimum value.");
        }
        if (voucher.getMaxUsage() > 0 && voucher.getUsedCount() >= voucher.getMaxUsage()) {
            throw new IllegalArgumentException("Voucher usage limit reached.");
        }
        return voucher;
    }

    @Override
    public List<Voucher> getAllVouchers() {
        return voucherDAO.findAll();
    }

    @Override
    @Transactional
    public Voucher saveVoucher(VoucherSaveRequest request) {
        Voucher voucher = request.voucherId() > 0
            ? voucherDAO.findById(request.voucherId()).orElse(new Voucher())
            : new Voucher();
        voucher.setCode(request.code());
        voucher.setDescription(request.description());
        voucher.setDiscountPercent(request.discountPercent());
        voucher.setDiscountAmount(request.discountAmount());
        voucher.setMinimumOrderValue(request.minimumOrderValue());
        voucher.setMaxUsage(request.maxUsage());
        voucher.setUsedCount(request.usedCount());
        voucher.setStartDate(request.startDate());
        voucher.setExpiryDate(request.expiryDate());
        voucher.setActive(request.active());
        return request.voucherId() > 0 ? voucherDAO.update(voucher) : voucherDAO.save(voucher);
    }

    @Override
    @Transactional
    public void markVoucherUsed(int voucherId) {
        voucherDAO.markUsed(voucherId);
    }
}
