package org.trinityfforce.sagopalgo.bid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.trinityfforce.sagopalgo.bid.entity.Bid;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid,Long> {

    List<Bid> findAllByItemId(Long itemId);

    List<Bid> findAllByUserId(Long userId);

    Bid findByUserIdAndItemId(Long userId, Long itemId);

    boolean existsByItemId(Long id);
}
