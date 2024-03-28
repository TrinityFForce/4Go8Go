package org.trinityfforce.sagopalgo.item.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.trinityfforce.sagopalgo.item.entity.Item;
import org.trinityfforce.sagopalgo.item.entity.QItem;

@RequiredArgsConstructor
public class ItemRepositoryQueryImpl implements ItemRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    @Transactional(readOnly = true)
    public List<Item> searchByName(String itemName) {
        QItem qItem = QItem.item;

        return jpaQueryFactory
            .selectFrom(qItem)
            .where(qItem.name.like("%" + itemName + "%"))
            .fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> searchByCategory(String categoryName) {
        QItem qItem = QItem.item;

        return jpaQueryFactory
            .selectFrom(qItem)
            .where(qItem.category.name.like(categoryName))
            .fetch();
    }

}
