package org.trinityfforce.sagopalgo.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.trinityfforce.sagopalgo.global.security.UserDetailsImpl;
import org.trinityfforce.sagopalgo.payment.dto.response.PaymentResponse;
import org.trinityfforce.sagopalgo.payment.service.PaymentService;

@Controller
@RequiredArgsConstructor
@Tag(name = "Payment API", description = "결제에 관련된 API")
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    @Operation(summary = "결제 목록 조회", description = "사용자의 결제 목록을 조회한다.")
    public ResponseEntity<List<PaymentResponse>> getPayment(@AuthenticationPrincipal
        UserDetailsImpl userDetails){
        return ResponseEntity.ok(paymentService.getPayment(userDetails.getUser()));
    }

}
