package org.trinityfforce.sagopalgo.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.trinityfforce.sagopalgo.item.entity.Item;
import org.trinityfforce.sagopalgo.payment.dto.response.TossPayment;
import org.trinityfforce.sagopalgo.user.entity.User;

@Entity
@Getter
@Table(name = "Payment")
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column
    private Integer price;

    @Column
    private boolean isPaid;

    @Column
    private String paidAt;

    @Column
    private String method;

    @Column
    private String orderId;

    @Column
    private String provider;

    @Column
    private String receipt;

    public Payment(User user, Item item, Integer price) {    //lambda가 경매종료 상태로 변경할때 함께 저장
        this.user = user;
        this.item = item;
        this.price = price;
        this.isPaid = false;
    }

    public void update(TossPayment tossPayment) {
        this.paidAt = tossPayment.getApprovedAt();
        this.method = tossPayment.getMethod();
        this.orderId = tossPayment.getOrderId();
        this.provider = tossPayment.getEasyPay().getProvider();
        this.receipt = tossPayment.getReceipt().getUrl();
        this.isPaid = true;
    }
}
