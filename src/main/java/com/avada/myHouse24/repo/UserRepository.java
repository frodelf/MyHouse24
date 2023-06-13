package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByOauthIdAndOauthProvider(int id, String provider);
    Optional<User> findByEmail(String email);
    Optional<User> findByFirstName(String email);
}
