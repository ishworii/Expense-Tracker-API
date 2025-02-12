package com.ishwor.expenses.repository;

import com.ishwor.expenses.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}