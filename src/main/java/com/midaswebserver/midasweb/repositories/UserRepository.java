package com.midaswebserver.midasweb.repositories;

import com.midaswebserver.midasweb.models.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @version 0.0.1
 * Vehicle to interact with the user table within the database
 * Through relations, other tables can be interacted with {@see Symbol}
 * @Author Aidan Scott
 * @since 0.0.1
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * @param username
     * @return User
     * @Author SpringBoot
     * @since 0.0.1
     * Finds User by username while ignoring case
     */
    List<User> findByUsernameIgnoreCase(String username);
}
