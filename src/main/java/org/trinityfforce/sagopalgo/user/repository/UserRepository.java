package org.trinityfforce.sagopalgo.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.trinityfforce.sagopalgo.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
}
