package com.gym.dao;

import com.gym.model.User;
import java.util.List;
import java.util.Optional;

public interface UserDAO {
    User save(User user);
    Optional<User> findById(int id);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    User update(User user);
    boolean delete(int id);
    boolean existsByUsername(String username);
}
