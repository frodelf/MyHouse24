package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.Chat;
import com.avada.myHouse24.entity.House;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Page<Chat> findAllByHouse(House house, Pageable pageable);
}
