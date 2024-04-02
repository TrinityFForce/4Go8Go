package org.trinityfforce.sagopalgo.item.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.trinityfforce.sagopalgo.item.dto.request.SearchRequest;
import org.trinityfforce.sagopalgo.item.entity.Item;
import org.trinityfforce.sagopalgo.item.entity.QItem;

@RequiredArgsConstructor
public class ItemRepositoryQueryImpl implements ItemRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;
    private QItem qItem = QItem.item;

    @Override
    @Transactional
    public List<Item> searchItem(SearchRequest searchRequest) {
        return jpaQueryFactory
            .selectFrom(qItem)
            .where(eqName(searchRequest.getName()),
                eqCategory(searchRequest.getCategory()))
            .fetch();
    }

    public BooleanExpression eqName(String itemName) {
        return itemName != null ? qItem.name.like(itemName + "%") : null;
    }

    public BooleanExpression eqCategory(String category) {
        return category != null ? qItem.category.name.like(category) : null;
    }


}
