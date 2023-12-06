package com.example.ecommerce.controller;

import com.example.ecommerce.model.CartProduct;
import com.example.ecommerce.model.Coupon;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.CouponService;
import com.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CouponService couponService;
    @GetMapping("cart")
    private String index(Model model) {
        var products = cartService.getCartItems();
        double discountValue = 0.0;

        if(cartService.getAppliedCoupon()!=null) {
            model.addAttribute("coupon", cartService.getAppliedCoupon());
            discountValue = cartService.getAppliedCoupon().getDiscount();
        } else {
            model.addAttribute("coupon", new Coupon());
        }
        double subTotal = 0;
        for (var product : products) {
            subTotal += product.getProduct().getPrice()*product.getQuantity();
        }
        if(products.size() == 0) {
            model.addAttribute("check", false);
        } else {
            model.addAttribute("check", true);
        }
        var appliedCoupon = Math.ceil((discountValue*subTotal)/100);
        var total = subTotal - appliedCoupon;
        model.addAttribute("appliedCoupon", appliedCoupon);
        model.addAttribute("subtotal", subTotal);
        model.addAttribute("total", total);
        model.addAttribute("courses", products);
        return "Cart/index";
    }
    @GetMapping("/cart/delete/{id}")
    public String deleteItemCart(@PathVariable Long id) {
        cartService.remove(id);
        return "redirect:/cart";
    }

    @PostMapping("/cart/{id}")
    public String addToCart(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Product product = productService.getProduct(id);
        if (product != null) {
            cartService.addProduct(product);
        }
        redirectAttributes.addFlashAttribute("notify", true);

        return "redirect:/products";
    }
    @PostMapping("/cart/product/update")
    public String updateCartProduct(CartProduct cartProduct,  RedirectAttributes redirectAttributes) {
        Product p = productService.getProduct(cartProduct.getProduct().getId());
        cartProduct.setProduct(p);
        cartService.addUpdate(cartProduct);
        redirectAttributes.addFlashAttribute("notify", true);

        return "redirect:/products/detail/"+cartProduct.getProduct().getId();
    }
    @PostMapping("/cart/update")
    @ResponseBody
    public String updateCartItem(@RequestParam Long id, @RequestParam Integer quantity) {
        // Use the "id" and "quantity" parameters to update the cart item in the service
        cartService.update(id, quantity);
        return "Cart/index";
    }

    @PostMapping("/cart/coupon")
    public String applyCoupon(Coupon c, RedirectAttributes redirectAttributes) {
        var coupon = couponService.getCouponByCode(c.getCode());

        if (coupon != null) {
            if(coupon.isValid()) {
                cartService.applyCoupon(coupon);
            } else {
                redirectAttributes.addFlashAttribute("validCoupon", false);
            }
        } else {
            cartService.applyCoupon(null);
        }
        return "redirect:/cart";
    }

}
