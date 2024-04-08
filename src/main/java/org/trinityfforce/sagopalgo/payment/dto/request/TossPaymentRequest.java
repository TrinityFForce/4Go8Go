package org.trinityfforce.sagopalgo.payment.dto.request;

import lombok.Data;

@Data
public class TossPaymentRequest {

    private String paymentKey;
    private String orderId;
    private Number amount;
}
