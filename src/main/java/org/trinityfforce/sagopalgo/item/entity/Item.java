package org.trinityfforce.sagopalgo.item.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
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

    @Column(nullable = false)
    private LocalDate startDate;

    @Column
    private LocalDate deadline;

    @Column
    private String url;

    @Column
    private Integer highestPrice;

    @Column
    private Integer viewCount;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemStatusEnum status;

    public Item(ItemRequest itemRequest, Category category, User user) {
        this.name = itemRequest.getName();
        this.startPrice = itemRequest.getStartPrice();
        this.bidUnit = itemRequest.getBidUnit();
        this.startDate = itemRequest.getStartDate();
        this.bidCount = 0;
        this.viewCount = 0;
        this.highestPrice = itemRequest.getStartPrice();
        this.category = category;
        this.user = user;
        this.status = ItemStatusEnum.PENDING;
    }

    public void update(ItemRequest itemRequest, Category category) {
        this.name = itemRequest.getName();
        this.startDate = itemRequest.getStartDate();
        this.startPrice = itemRequest.getStartPrice();
        this.bidUnit = itemRequest.getBidUnit();
        this.highestPrice = itemRequest.getStartPrice();
        this.category = category;
    }

    public void updateBidItem(Integer price) {
        this.highestPrice = price;
    }

    public void addViewCount(){
        this.viewCount++;
    }
}
