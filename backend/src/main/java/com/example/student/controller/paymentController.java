package com.example.student.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.student.dto.paymentDetails;
import com.example.student.dto.paymentResponse;
import com.example.student.service.paymentService;

@RestController
@RequestMapping("/product/v1")
public class paymentController {
    

    private paymentService stripeService;

    public paymentController( paymentService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<paymentResponse> checkoutProducts(@RequestBody paymentDetails productRequest) {
        productRequest.setQuantity((long)0);
        System.out.println("hitting the payment method");
        paymentResponse stripeResponse = stripeService.checkoutProducts(productRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(stripeResponse);
    }
}
