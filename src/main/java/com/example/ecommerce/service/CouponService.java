package com.example.ecommerce.service;

import com.example.ecommerce.model.Coupon;
import com.example.ecommerce.repo.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public Optional<Coupon> getCouponById(Long id) {
        return couponRepository.findById(id);
    }

    public Coupon createCoupon(Coupon coupon) {
        // Additional validation can be added here
        return couponRepository.save(coupon);
    }

    public Coupon updateCoupon(Long id, Coupon updatedCoupon) {
        // Additional validation can be added here
        Optional<Coupon> existingCoupon = couponRepository.findById(id);
        if (existingCoupon.isPresent()) {
            Coupon coupon = existingCoupon.get();
            coupon.setCode(updatedCoupon.getCode());
            coupon.setDiscount(updatedCoupon.getDiscount());
            coupon.setExpirationDate(updatedCoupon.getExpirationDate());
            return couponRepository.save(coupon);
        } else {
            // Handle not found
            throw new RuntimeException("Coupon not found with id: " + id);
        }
    }

    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id);
    }
    public Coupon getCouponByCode(String code) {
        return couponRepository.getCouponByCode(code);
    }
    public List<Coupon> getValidCoupons() {
        Date currentDate = new Date();
        return couponRepository.findByExpirationDateAfter(currentDate);
    }
}

