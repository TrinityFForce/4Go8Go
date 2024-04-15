package org.trinityfforce.sagopalgo.item.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.trinityfforce.sagopalgo.item.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryQuery {
    @Modifying
    @Query("UPDATE Item i SET i.bidCount = i.bidCount + 1, i.highestPrice = :price WHERE i.id = :id")
    void updateItem(@Param("id") Long id, @Param("price") Integer price);

    List<Item> findAllByUserId(Long id);
}
