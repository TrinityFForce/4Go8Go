package org.trinityfforce.sagopalgo.payment.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.trinityfforce.sagopalgo.payment.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findAllByUserId(Long id);
}
