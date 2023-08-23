package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
