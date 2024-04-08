package org.trinityfforce.sagopalgo.payment.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trinityfforce.sagopalgo.payment.dto.response.PaymentInfoResponse;
import org.trinityfforce.sagopalgo.payment.dto.response.PaymentResponse;
import org.trinityfforce.sagopalgo.payment.entity.Payment;
import org.trinityfforce.sagopalgo.payment.repository.PaymentRepository;
import org.trinityfforce.sagopalgo.user.entity.User;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPayment(User user) {
        List<Payment> paymentList = paymentRepository.findAllByUserId(user.getId());
        return paymentList.stream().map(payment -> new PaymentResponse(payment))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PaymentInfoResponse getPaymentInfo(User user, Long paymentId)
        throws BadRequestException {
        Payment payment = getPayment(paymentId);
        isAuthorized(payment, user);
        return new PaymentInfoResponse(payment);
    }

    private Payment getPayment(Long pamentId) {
        return paymentRepository.findById(pamentId).orElseThrow(
            () -> new NullPointerException("해당 결제정보가 존재하지 않습니다.")
        );
    }

    private void isAuthorized(Payment payment, User user) throws BadRequestException {
        if (!payment.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("해당 결제를 진핼할 권한이 없습니다.");
        }
    }
}
