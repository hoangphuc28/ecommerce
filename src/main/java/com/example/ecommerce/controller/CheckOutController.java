package com.example.ecommerce.controller;

import com.example.ecommerce.model.*;
import com.example.ecommerce.service.*;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;

@Controller
public class CheckOutController {
    private double ship = 30000;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private OrderProductService orderProductService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PaypalService paypalService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CustomUserDetailsService userDetailsService;
    @PostMapping("/checkout")
    public String checkout(Model model, @RequestParam String total, @RequestParam String coupon) {
        model.addAttribute("products", cartService.getCartItems());
        model.addAttribute("orderTotal", Double.parseDouble(total)+ship);
        model.addAttribute("coupon", Double.parseDouble(coupon));
        model.addAttribute("ship", ship);
        model.addAttribute("order", new Order());
        return "checkout/index";
    }
    @PostMapping("/checkout/confirm")
    public String checkoutConfirm(@ModelAttribute("order") Order o, Principal principal) throws MessagingException, IOException {
        var products = cartService.getCartItems();
        PaymentMethod p = paymentMethodService.getPaymentMethodById(o.getPaymentMethod().getId());
        double total = 0;
        if(principal != null) {
            User user = userDetailsService.getUserByString(principal.getName());
            o.getCustomer().setUser(user);
        }
        Customer c = customerService.createCustomer(o.getCustomer());
        Order order = new Order();
        order.setPaymentMethod(p);
        order.setOrderDate(new Date());
        order.setCustomer(c);
        double discountValue = 0.0;
        if(cartService.getAppliedCoupon()!=null) {
            order.setCoupon(cartService.getAppliedCoupon());
            discountValue = cartService.getAppliedCoupon().getDiscount();
        }
        order.setStatus(OrderStatus.DELIVERING);
        orderService.createOrder(order);
        for (var product : products) {
            OrderProduct op = new OrderProduct();
            op.setOrder(order);
            op.setProduct(product.getProduct());
            op.setQuantity(product.getQuantity());
            orderProductService.createOrderProduct(op);
            total+= product.getProduct().getPrice()*product.getQuantity();
            product.getProduct().setQuantity(product.getProduct().getQuantity()-product.getQuantity());
            productService.update(product.getProduct());
        }
        total = (total+ship)-(total*discountValue)/100;
        order.setTotalAmount(BigDecimal.valueOf(total));
        orderService.updateOrder(order.getId(), order);
        if(p.getId() == 1) {
            orderService.updateOrder(order.getId(), order);
            cartService.removeAll();
            emailService.sendOrderDetail(c.getEmail(), "Order Tracking", order);
        } else {
            order.setStatus(OrderStatus.PAYMENT_PENDING);
            orderService.updateOrder(order.getId(), order);
            var orderEncrypted = tokenService.generateTokenOrder(order.getId());
            var data = paypalService.createJsonPayload(Double.valueOf(total), "http://localhost:8080/checkout/success?order="+orderEncrypted, "http://localhost:8080/checkout/failed?order="+orderEncrypted);
            var url = (paypalService.getApproveLink(paypalService.createOrder(data)));
            return "redirect:"+url;
        }
        return "Home/index";
    }
    @GetMapping("/checkout/failed")
    private String failedPayment() {
        return "Notify/failed-payment";
    }
    @GetMapping("/checkout/success")
    private String successPayment(@RequestParam String token, @RequestParam String order) {
        var orderId = tokenService.decryptTokenOrder(order);
        var orderObject = orderService.getOrderById(Long.valueOf(orderId));
        orderObject.setStatus(OrderStatus.DELIVERING);
        orderService.updateOrder(orderObject.getId(), orderObject);
        var url = paypalService.capturePayment(token);
        System.out.println(url);
        cartService.removeAll();
        return "Notify/success-payment";
    }
}
