package org.trinityfforce.sagopalgo.bid.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.trinityfforce.sagopalgo.global.common.Timestamped;
import org.trinityfforce.sagopalgo.item.entity.Item;
import org.trinityfforce.sagopalgo.user.entity.User;

@Entity
@Table(name = "Bids")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Bid extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Integer price;

    public Bid(Long itemId, User user, Integer price) {
        this.itemId = itemId;
        this.user = user;
        this.price = price;
    }

    public void updatePrice(Integer price) {
        this.price = price;
    }
}
