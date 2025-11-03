package com.ashish.library_management_system.service;

import com.ashish.library_management_system.model.Book;
import com.ashish.library_management_system.model.BookSpecification;
import com.ashish.library_management_system.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService{

    @Autowired
    BookRepository bookRepository;

    @Override
    @Transactional
    public synchronized void addBook(Book book) {
        Optional<Book> bookOptional = bookRepository.findByTitle(book.getTitle());
        if(bookOptional.isPresent()){
            Book dbBook = bookOptional.get();
            dbBook.setTotalCopies(dbBook.getTotalCopies()+1);
            dbBook.setAvailableCopies(dbBook.getAvailableCopies()+1);
            bookRepository.save(dbBook);
        }
        else{
            bookRepository.save(book);
        }
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).get();
    }


    public List<Book> getBooksByFilters(Map<String,String> parameterMap, Pageable pageable){

        Specification<Book> bookSpecification = BookSpecification.applyFilters(parameterMap);
        return bookRepository.findAll(bookSpecification,pageable).getContent();
    }

    @Override
    @Transactional
    public void updateBook(Book bookWithNewData, long id) {
        Book bookFromDB = bookRepository.findById(id).get();
        bookFromDB.setTitle(bookWithNewData.getTitle());
        bookFromDB.setAuthor(bookWithNewData.getAuthor());
        bookFromDB.setCategory(bookWithNewData.getCategory());
        bookFromDB.setTotalCopies(bookWithNewData.getTotalCopies());
        bookFromDB.setAvailable(bookWithNewData.isAvailable());
        bookFromDB.setAvailableCopies(bookWithNewData.getAvailableCopies());
        bookRepository.save(bookFromDB);
    }

    @Override
    public void deleteBook(long id) {
        Book book =  getBookById(id);
         synchronized (this){
             if(book.getTotalCopies()==book.getAvailableCopies()){
                 bookRepository.delete(book);
             }
         }
    }


}
