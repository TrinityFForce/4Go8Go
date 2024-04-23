package org.trinityfforce.sagopalgo.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trinityfforce.sagopalgo.global.security.UserDetailsImpl;
import org.trinityfforce.sagopalgo.payment.dto.request.TossPaymentRequest;
import org.trinityfforce.sagopalgo.payment.dto.response.PaymentInfoResponse;
import org.trinityfforce.sagopalgo.payment.service.TossService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Toss API", description = "결제에 관련된 API")
@RequestMapping("/api/v1/payments")
public class TossController {

    private final TossService tossService;

    @GetMapping("/{paymentId}/toss/confirm")
    @Operation(summary = "토스 결제", description = "결제 ID를 통해 토스 결제를 진행한다.")
    public ResponseEntity<PaymentInfoResponse> makePayment(
        @AuthenticationPrincipal UserDetailsImpl userDetails, @ModelAttribute
    TossPaymentRequest tossPaymentRequest,
        @PathVariable Long paymentId) //FE에서 받은 paymentKey, amount, orderId
        throws BadRequestException {
        return ResponseEntity.ok(
            tossService.makePayment(paymentId, userDetails.getUser(), tossPaymentRequest));
    }
}
