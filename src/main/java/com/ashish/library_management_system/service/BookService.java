package com.ashish.library_management_system.service;

import com.ashish.library_management_system.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;


public interface BookService {
    void addBook(Book book);

    Book getBookById(Long id);

    List<Book> getBooksByFilters(Map<String,String> parameterMap, Pageable pageable);

    void updateBook(Book bookWithNewData, long id);

    void deleteBook(long id);
}
