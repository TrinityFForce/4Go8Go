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
    private String itemName;
    private Integer price;
    private boolean isPaid;
    private String url;

    public PaymentResponse(Payment payment) {
        this.id = payment.getId();
        this.itemName = payment.getItem().getName();
        this.price = payment.getPrice();
        this.isPaid = payment.isPaid();
        this.url = payment.getItem().getUrl();
    }
}
