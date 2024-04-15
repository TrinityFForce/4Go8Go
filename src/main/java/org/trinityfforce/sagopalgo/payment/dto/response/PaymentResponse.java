package org.trinityfforce.sagopalgo.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.trinityfforce.sagopalgo.payment.entity.Payment;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    private Long id;
    private Long itemId;
    private String itemName;
    private Integer price;
    private boolean isPaid;
    private String paidAt;
    private String method;
    private String orderId;
    private String provider;
    private String receipt;

    public PaymentResponse(Payment payment) {
        this.itemId = payment.getItem().getId();
        this.itemName = payment.getItem().getName();
        this.price = payment.getPrice();
        this.isPaid = payment.isPaid();
        this.paidAt = payment.getPaidAt();
        this.method = payment.getMethod();
        this.orderId = payment.getOrderId();
        this.provider = payment.getProvider();
        this.receipt = payment.getReceipt();
    }
}
