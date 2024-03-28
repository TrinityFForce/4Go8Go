package org.trinityfforce.sagopalgo.item.repository;

import java.util.List;
import org.trinityfforce.sagopalgo.item.entity.Item;

public interface ItemRepositoryQuery {

    List<Item> searchByName(String itemName);

    List<Item> searchByCategory(String category);

}
