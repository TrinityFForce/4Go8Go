package org.trinityfforce.sagopalgo.item.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.trinityfforce.sagopalgo.item.dto.request.SearchRequest;
import org.trinityfforce.sagopalgo.item.dto.response.ItemResponse;
import org.trinityfforce.sagopalgo.item.entity.Item;
import org.trinityfforce.sagopalgo.item.entity.ItemStatusEnum;
import org.trinityfforce.sagopalgo.item.entity.QItem;
import org.trinityfforce.sagopalgo.item.entity.RestPage;

@RequiredArgsConstructor
public class ItemRepositoryQueryImpl implements ItemRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;
    private QItem qItem = QItem.item;

    @Override
    @Transactional
    public Page<ItemResponse> pageItem(SearchRequest searchRequest, Pageable pageable) {
        QueryResults<Item> queryResults = jpaQueryFactory
            .selectFrom(qItem)
            .where(eqName(searchRequest.getName()),
                eqCategory(searchRequest.getCategory()),
                eqStatus(searchRequest.getStatus()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(itemSort(pageable))
            .fetchResults();
        List<Item> content = queryResults.getResults();
        List<ItemResponse> itemResponseList = content.stream().map(item -> new ItemResponse(item))
            .collect(
                Collectors.toList());
        Long total = queryResults.getTotal();
        return new RestPage<>(itemResponseList, pageable.getPageNumber(), pageable.getPageSize(),
            total);
    }

    @Override
    @Transactional
    public List<Item> getItem(LocalDate date, String condition) {
        return jpaQueryFactory
            .selectFrom(qItem)
            .where(eqCondition(condition),
                qItem.startDate.eq(date))
            .fetch();
    }

    private BooleanExpression eqCondition(String condition) {
        switch (condition) {
            case "before":
                return qItem.status.eq(ItemStatusEnum.PENDING);

            case "progress":
                return qItem.status.eq(ItemStatusEnum.INPROGRESS);

            case "after":
                return qItem.status.eq(ItemStatusEnum.COMPLETED);
        }
        return null;
    }

    private BooleanExpression eqName(String itemName) {
        return itemName != null ? qItem.name.like(itemName + "%") : null;
    }

    private BooleanExpression eqCategory(String category) {
        return category != null ? qItem.category.name.like(category) : null;
    }

    private BooleanExpression eqStatus(String status) {
        return status != null ? qItem.status.eq(ItemStatusEnum.valueOf(status)) : null;
    }

    private OrderSpecifier<?> itemSort(Pageable pageable) {
        if (!pageable.getSort().isEmpty()) {
            Sort.Order order = pageable.getSort().iterator().next();
            Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
            switch (order.getProperty()) {
                case "bidcount":
                    return new OrderSpecifier(direction, qItem.bidCount);

                case "highestprice":
                    return new OrderSpecifier(direction, qItem.highestPrice);

                case "viewcount":
                    return new OrderSpecifier(direction, qItem.viewCount);
            }
        }
        return new OrderSpecifier(Order.DESC, qItem.viewCount);
    }

}
