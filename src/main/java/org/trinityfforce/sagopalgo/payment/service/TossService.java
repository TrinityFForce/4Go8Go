package org.trinityfforce.sagopalgo.payment.service;


import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.trinityfforce.sagopalgo.payment.dto.request.TossPaymentRequest;
import org.trinityfforce.sagopalgo.payment.dto.response.PaymentResponse;
import org.trinityfforce.sagopalgo.payment.dto.response.TossPayment;
import org.trinityfforce.sagopalgo.payment.entity.Payment;
import org.trinityfforce.sagopalgo.payment.repository.PaymentRepository;
import org.trinityfforce.sagopalgo.user.entity.User;

@Service
@RequiredArgsConstructor
public class TossService {

    @Value("${toss.secret.key}")
    private String tossSecretKey;

    public static final String URL = "https://api.tosspayments.com/v1/payments/confirm";
    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentResponse makePayment(Long paymentId, User user,
        TossPaymentRequest tossPaymentRequest)        //paymentKey, orderId, amount
        throws BadRequestException {
        Payment payment = getPayment(paymentId);
        isAuthorized(payment, user);
        String authorization = Base64.getEncoder()
            .encodeToString(tossSecretKey.getBytes());    //TOSS_SECRET Base64 인코딩
        RestTemplate restTemplate = new RestTemplate();

        //헤더구성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + authorization);     //TOSS_SECRET
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        //요청 객체 생성
        HttpEntity<TossPaymentRequest> requestHttpEntity = new HttpEntity<>(tossPaymentRequest,
            headers);

        //응답 객체 TossPayment객체로 결제 응답받기
        TossPayment tossPayment = restTemplate.postForObject(URL, requestHttpEntity,
            TossPayment.class);

        payment.update(
            tossPayment);    //결제 응답에서 필요한 부분만 사용하여 업데이트(receipt, paidAt, method, orderId, provider)
        return new PaymentResponse(payment);
    }

    private Payment getPayment(Long pamentId) throws BadRequestException {
        return paymentRepository.findById(pamentId).orElseThrow(
            () -> new BadRequestException("해당 결제정보가 존재하지 않습니다.")
        );
    }

    private void isAuthorized(Payment payment, User user) throws BadRequestException {
        if (!payment.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("해당 결제를 진핼할 권한이 없습니다.");
        }
    }
}
