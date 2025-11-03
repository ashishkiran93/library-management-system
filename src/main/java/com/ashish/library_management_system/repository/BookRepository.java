package com.ashish.library_management_system.repository;

import com.ashish.library_management_system.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> , JpaSpecificationExecutor<Book> {

    Optional<Book> findByTitle(String title);
}
