package org.trinityfforce.sagopalgo.payment.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
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
    private Integer price;
    private boolean isPaid;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime paymentTime;
    private String paymentMethod;

    public PaymentResponse(Payment payment){
        this.itemId = payment.getItem().getId();
        this.price = payment.getPrice();
        this.isPaid = payment.isPaid();
        this.paymentTime = payment.getPaymentTime();
        this.paymentMethod = payment.getPaymentMethod();
    }
}
