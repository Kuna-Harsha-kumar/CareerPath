package com.example.student.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class paymentDetails {
    
    private Long amount;
    private Long quantity;
    private String name;
    private String currency;
}
