package com.example.student.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.student.dto.paymentDetails;
import com.example.student.dto.paymentResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class paymentService {

    @Value("${stripe.secretKey}")
    private String secretKey;

     public paymentResponse checkoutProducts(paymentDetails productRequest) {
            // Set your secret key. Remember to switch to your live secret key in production!
            Stripe.apiKey = secretKey;

            // Create a PaymentIntent with the order amount and currency
            SessionCreateParams.LineItem.PriceData.ProductData productData =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(productRequest.getName())
                            .build();

            // Create new line item with the above product data and associated price
            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency(productRequest.getCurrency() != null ? productRequest.getCurrency() : "USD")
                            .setUnitAmount(productRequest.getAmount())
                            .setProductData(productData)
                            .build();

            // Create new line item with the above price data
            SessionCreateParams.LineItem lineItem =
                    SessionCreateParams
                            .LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(priceData)
                            .build();
            // Create new session with the line items
            SessionCreateParams params =
                    SessionCreateParams.builder()
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setSuccessUrl("http://localhost:5173/appointments")
                            .setCancelUrl("http://localhost:8080/cancel")
                            .addLineItem(lineItem)
                            .build();

            // Create new session
            Session session = null;
            try {
                session = Session.create(params);
            } catch (StripeException e) {
                System.out.println(e.getMessage());
            }

            return paymentResponse.builder()
                    .status("SUCCESS")
                    .message("Payment session created ")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();
        }

}
