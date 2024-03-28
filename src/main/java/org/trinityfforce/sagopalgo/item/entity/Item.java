package org.trinityfforce.sagopalgo.item.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.trinityfforce.sagopalgo.category.entity.Category;
import org.trinityfforce.sagopalgo.global.common.Timestamped;
import org.trinityfforce.sagopalgo.item.dto.request.ItemRequest;
import org.trinityfforce.sagopalgo.user.entity.User;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Item")
@SQLDelete(sql = "UPDATE Item SET deleted_at=CURRENT_TIMESTAMP where id=?")
@Where(clause = "deleted_at IS NULL")
public class Item extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer startPrice;

    @Column(nullable = false)
    private Integer bidUnit;

    @Column(nullable = false)
    private Integer bidCount;

    @Column
    private LocalDateTime deadline;

    @Column
    private Integer highestPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Item(ItemRequest itemRequest, Category category, User user) {
        this.name = itemRequest.getName();
        this.startPrice = itemRequest.getStartPrice();
        this.bidUnit = itemRequest.getBidUnit();
        this.bidCount = 0;
        this.deadline = itemRequest.getDeadLine();
        this.highestPrice = itemRequest.getStartPrice();
        this.category = category;
        this.user = user;
    }

    public void update(ItemRequest itemRequest, Category category) {
        this.name = itemRequest.getName();
        this.startPrice = itemRequest.getStartPrice();
        this.bidUnit = itemRequest.getBidUnit();
        this.deadline = itemRequest.getDeadLine();
        this.highestPrice = itemRequest.getStartPrice();
        this.category = category;
    }

    public void updateBidItem(Integer price) {
        this.highestPrice = price;
        this.bidCount++;
    }
}
