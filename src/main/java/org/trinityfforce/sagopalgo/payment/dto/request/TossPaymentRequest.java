package org.trinityfforce.sagopalgo.payment.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TossPaymentRequest {

    private String paymentKey;
    private String orderId;
    private Number amount;
}
