package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.pages.AboutPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<AboutPage.Document, Long> {
}
