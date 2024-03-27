package org.trinityfforce.sagopalgo.bid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.trinityfforce.sagopalgo.bid.entity.Bid;

public interface BidRepository extends JpaRepository<Bid,Long> {

    boolean existsByItemId(Long id);
}
