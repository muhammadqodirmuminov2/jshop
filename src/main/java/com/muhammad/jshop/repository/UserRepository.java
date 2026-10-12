package com.muhammad.jshop.repository;

import com.muhammad.jshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
