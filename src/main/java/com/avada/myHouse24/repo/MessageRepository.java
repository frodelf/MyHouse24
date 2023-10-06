package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findAllByHouse(House house, Pageable pageable);
}
