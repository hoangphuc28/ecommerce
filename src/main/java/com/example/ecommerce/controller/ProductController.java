package com.example.ecommerce.controller;

import com.example.ecommerce.model.CartProduct;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.Review;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.CategoryService;
import com.example.ecommerce.service.CustomUserDetailsService;
import com.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProductController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomUserDetailsService userService;
    @GetMapping("/products")
    private String index(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "0") Long category, Model model, Principal principal) {
        PageRequest pageable = PageRequest.of(page-1, 9);
        Page<Product> products = productService.paginateProduct(category, pageable);
        model.addAttribute("filterCategory", category);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("products", products.getContent());
        model.addAttribute("currentPage", page-1);
        model.addAttribute("totalPages", products.getTotalPages()-1);
        List<Product> bestSellerProducts = products.stream().
                sorted(Comparator.comparingInt(Product::calculateTotalSoldQuantity).reversed()).filter(p -> p.calculateTotalSoldQuantity()>0).limit(10).collect(Collectors.toList());
        model.addAttribute("besSellerProducts", bestSellerProducts);
        return "Product/index";
    }

    @GetMapping("products/detail/{id}")
    private String detailProduct(@PathVariable Long id, Model model) {
        var product = productService.getProduct(id);
        String imageUrl = String.format("/img/bg-img/");
        String imageProductUrl = String.format("/img/");
        // Add the image URL to the model for use in your Thymeleaf template
        model.addAttribute("imageUrl", imageUrl);
        model.addAttribute("imageProductUrl", imageProductUrl);
        CartProduct cp = new CartProduct();
        cp.setQuantity(1);
        cp.setProduct(product);
        model.addAttribute("CartProduct", cp);
        model.addAttribute("product", product);
        var review = new Review();
        review.setProduct(product);
        model.addAttribute("review", review);
        return "Product/product-detail";
    }

}
