package org.trinityfforce.sagopalgo.item.repository;

import java.util.List;
import org.trinityfforce.sagopalgo.item.dto.request.SearchRequest;
import org.trinityfforce.sagopalgo.item.entity.Item;

public interface ItemRepositoryQuery {

    List<Item> searchItem(SearchRequest searchRequest);

}
