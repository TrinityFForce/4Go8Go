package org.trinityfforce.sagopalgo.item.repository;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.trinityfforce.sagopalgo.item.dto.request.SearchRequest;
import org.trinityfforce.sagopalgo.item.dto.response.ItemResponse;

public interface ItemRepositoryQuery {

    Page<ItemResponse> getItem(LocalDate date, String condition, Pageable pageable);

    Page<ItemResponse> pageItem(SearchRequest searchRequest, Pageable pageable);

}
