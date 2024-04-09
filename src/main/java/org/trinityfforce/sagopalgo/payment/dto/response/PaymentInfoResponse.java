package org.trinityfforce.sagopalgo.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.trinityfforce.sagopalgo.payment.entity.Payment;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfoResponse {

    private Long itemId;
    private String itemName;
    private Integer price;
    private String username;
    private boolean isPaid;
    private String paidAt;
    private String method;
    private String orderId;
    private String provider;
    private String receipt;

    public PaymentInfoResponse(Payment payment) {
        this.itemId = payment.getId();
        this.itemName = payment.getItem().getName();
        this.price = payment.getPrice();
        this.username = payment.getItem().getUser().getUsername();
        this.isPaid = payment.isPaid();
        this.paidAt = payment.getPaidAt();
        this.method = payment.getMethod();
        this.orderId = payment.getOrderId();
        this.provider = payment.getProvider();
        this.receipt = payment.getReceipt();
    }
}
