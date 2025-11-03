package com.ashish.library_management_system.controller;


import com.ashish.library_management_system.model.Book;
import com.ashish.library_management_system.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping
public class BookController {
    @Autowired
    BookService bookService;

    @PostMapping("/book")
    private void addNewBook(@RequestBody Book book){
      log.info("Adding new books to DB");
      bookService.addBook(book);
      log.info("Book with title {} added sucessfully",book.getTitle());
    }


    @GetMapping("/book")
    List<Book> getBookListByFilters(
            @RequestParam Map<String,String> allParameters,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "title") String sortedBy,
            @RequestParam(defaultValue = "asc") String sortDir
            ){
        log.info("Fetching books with request parameters");
        //Removing "page","size",sortedBy",sortDir"
        Map<String,String> filters = allParameters
                .entrySet().stream()
                .filter(e->!List.of("page","size","sortedBy","sortDir").contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));

        //Creating Sort object
        Sort sort = sortDir.equalsIgnoreCase("desc")?Sort.by(sortedBy).descending():Sort.by(sortedBy).ascending();


        //Creating page object
        Pageable pageable = PageRequest.of(page,size,sort);

        List<Book> searchedBookList = bookService.getBooksByFilters(filters,pageable);
        return searchedBookList;
    }

    @PutMapping("/book/{id}")
    ResponseEntity<String> updateBookDetails(@RequestBody Book bookWithNewData, @PathVariable("id") long id ){
    log.info("Fetching books by Id");
    bookService.updateBook(bookWithNewData,id);
    return ResponseEntity.ok("Data updated");
    }

    @DeleteMapping("book/{id}")
    ResponseEntity<String> deleteBook(@PathVariable("id") long id){
      log.info("Deleting books with given id");
      bookService.deleteBook(id);
      return ResponseEntity.ok("book deleted");
    }
}
