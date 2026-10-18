package com.gameflix.gameflixauth.repository;

import com.gameflix.gameflixauth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Database access layer for users.
// Provides built-in database methods for the User table; ex: save, find, and delete.
public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findByEmail(String email);
    // allows Spring generate a query to look up a user by email
}
