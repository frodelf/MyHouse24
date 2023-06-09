package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByOauthIdAndOauthProvider(int id, String provider);
    User findByEmail(String email);
}
