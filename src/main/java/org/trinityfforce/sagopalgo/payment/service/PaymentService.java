package org.trinityfforce.sagopalgo.payment.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.trinityfforce.sagopalgo.payment.dto.response.PaymentResponse;
import org.trinityfforce.sagopalgo.payment.entity.Payment;
import org.trinityfforce.sagopalgo.payment.repository.PaymentRepository;
import org.trinityfforce.sagopalgo.user.entity.User;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public List<PaymentResponse> getPayment(User user) {
        List<Payment> paymentList = paymentRepository.findAllByUserId(user.getId());
        return paymentList.stream().map(payment -> new PaymentResponse(payment)).collect(Collectors.toList());
    }
}
