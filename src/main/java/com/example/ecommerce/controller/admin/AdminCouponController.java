package com.example.ecommerce.controller.admin;

import com.example.ecommerce.model.Coupon;
import com.example.ecommerce.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/coupons")
public class AdminCouponController {

    @Autowired
    private CouponService couponService;

    @GetMapping
    public String getAllCoupons(Model model) {
        model.addAttribute("coupons", couponService.getAllCoupons());
        return "Admin/Coupon/index";
    }

    @GetMapping("/{id}")
    public String getCouponById(@PathVariable Long id, Model model) {
        Optional<Coupon> coupon = couponService.getCouponById(id);
        model.addAttribute("coupon", coupon);
        return "coupon/view";
    }

    @GetMapping("/add")
    public String createCouponForm(Model model) {
        model.addAttribute("err", "");
        model.addAttribute("coupon", new Coupon());
        return "Admin/Coupon/add";
    }

    @PostMapping("/add")
    public String createCoupon(Coupon coupon, Model model) {
        if(couponService.getCouponByCode(coupon.getCode())!= null) {
            model.addAttribute("err", "Coupon has existed!");
            model.addAttribute("coupon", coupon);
            return "Admin/Coupon/add";

        }

        couponService.createCoupon(coupon);
        return "redirect:/admin/coupons";
    }

    @GetMapping("/{id}/edit")
    public String editCouponForm(@PathVariable Long id, Model model) {
        Optional<Coupon> coupon = couponService.getCouponById(id);
        model.addAttribute("coupon", coupon);
        return "coupon/edit";
    }

    @PostMapping("/{id}/edit")
    public String editCoupon(@PathVariable Long id, @ModelAttribute Coupon updatedCoupon) {
        couponService.updateCoupon(id, updatedCoupon);
        return "redirect:/coupons";
    }

    @GetMapping("/{id}/delete")
    public String deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return "redirect:/coupons";
    }
}

