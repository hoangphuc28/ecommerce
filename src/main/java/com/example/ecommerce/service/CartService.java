package com.example.ecommerce.service;

import com.example.ecommerce.model.CartProduct;
import com.example.ecommerce.model.Coupon;
import com.example.ecommerce.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Service
@SessionScope
public class CartService {
    private List<CartProduct> cartItems;
    private Coupon appliedCoupon;

    public void applyCoupon(Coupon coupon) {
        this.appliedCoupon = coupon;
    }

    public Coupon getAppliedCoupon() {
        return appliedCoupon;
    }
    public CartService() {
        cartItems = new ArrayList<>();
    }

    public void addProduct(Product book) {
        for (CartProduct item : cartItems) {
            if (item.getProduct().getId().equals(book.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        CartProduct newItem = new CartProduct();
        newItem.setProduct(book);
        newItem.setQuantity(1);
        cartItems.add(newItem);
    }
    public void remove(Long id) {
        for(var item : cartItems) {
            if(item.getProduct().getId()==id) {
                cartItems.remove(item);
                break;
            }
        }
    }
    public void update(Long id, int quantity) {
        for(int i = 0; i < this.cartItems.size(); i++) {
            if(this.cartItems.get(i).getProduct().getId()==id) {
                this.cartItems.get(i).setQuantity(quantity);
                return;
            }
        }
    }
    public void addUpdate(CartProduct cartProduct) {
        for (CartProduct item : cartItems) {
            if (item.getProduct().getId().equals(cartProduct.getProduct().getId())) {
                item.setQuantity(item.getQuantity() + cartProduct.getQuantity());
                return;
            }
        }
        cartItems.add(cartProduct);
    }

    public List<CartProduct> getCartItems() {
        return cartItems;
    }
    public void removeAll() {
        cartItems = new ArrayList<>();
    }
}
