package com.techguy.service.impl;

import com.techguy.entity.Payment;
import com.techguy.repository.PaymentRepository;
import com.techguy.service.PaymentServie;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentServie {
    private final PaymentRepository paymentRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }
}
