package org.trinityfforce.sagopalgo.item.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.trinityfforce.sagopalgo.category.entity.Category;
import org.trinityfforce.sagopalgo.item.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByCategory(Category category);
}
