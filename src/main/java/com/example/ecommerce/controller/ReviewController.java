package com.example.ecommerce.controller;

import com.example.ecommerce.model.Review;
import com.example.ecommerce.service.CustomUserDetailsService;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Date;

@Controller
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private ProductService productService;

    @PostMapping("/review")
    private String review(Review review, Principal principal) {
        System.out.println(review);
        var user = userDetailsService.getUserByString(principal.getName());
        var product = productService.getProduct(review.getProduct().getId());
        review.setUser(user);
        review.setReviewDate(new Date());
        review.setProduct(product);

        reviewService.saveReview(review);

        return "redirect:/products/detail/"+review.getProduct().getId();
    }
}
