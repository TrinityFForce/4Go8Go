package org.trinityfforce.sagopalgo.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.trinityfforce.sagopalgo.item.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryQuery {


}
