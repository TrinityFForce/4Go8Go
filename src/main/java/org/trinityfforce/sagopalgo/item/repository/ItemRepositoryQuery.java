package org.trinityfforce.sagopalgo.item.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.trinityfforce.sagopalgo.item.dto.request.SearchRequest;
import org.trinityfforce.sagopalgo.item.dto.response.ItemInfoResponse;
import org.trinityfforce.sagopalgo.item.dto.response.ItemResponse;
import org.trinityfforce.sagopalgo.item.entity.Item;

public interface ItemRepositoryQuery {

    List<Item> getItem(LocalDate date, String condition);

    Page<ItemResponse> pageItem(SearchRequest searchRequest, Pageable pageable);

}
