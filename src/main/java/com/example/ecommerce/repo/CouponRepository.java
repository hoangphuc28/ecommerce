package com.example.ecommerce.repo;

import com.example.ecommerce.model.Coupon;
import com.example.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findByExpirationDateAfter(Date currentDate);

    @Query("SELECT c FROM Coupon c WHERE c.code = ?1")
    public Coupon getCouponByCode(@Param("code") String code);
}

