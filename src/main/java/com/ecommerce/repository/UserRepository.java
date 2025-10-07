package com.ecommerce.repository;

import com.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Essential finder methods
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    
    // Essential existence checks
    boolean existsByEmail(String email);
    boolean existsByName(String name);
    
    // Combined search for login (email or username)
    @Query("SELECT u FROM User u WHERE u.email = :identifier OR u.name = :identifier")
    Optional<User> findByEmailOrName(@Param("identifier") String identifier);
}