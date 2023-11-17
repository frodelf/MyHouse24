package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAll();
    Page<Message> findAll(Pageable pageable);
    @Query("SELECT m FROM Message m WHERE (:searchQuery IS NULL OR :searchQuery = '' OR m.text LIKE %:searchQuery%) OR (:searchQuery IS NULL OR :searchQuery = '' OR m.topic LIKE %:searchQuery%)")
    Page<Message> findByTextOrTopicContaining(String searchQuery, Pageable pageable);
}
