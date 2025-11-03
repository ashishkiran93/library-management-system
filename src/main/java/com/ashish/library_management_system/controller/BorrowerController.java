package com.ashish.library_management_system.controller;


import com.ashish.library_management_system.dto.BorrowerBooksDTO;
import com.ashish.library_management_system.exception.BorrowerNotFoundException;
import com.ashish.library_management_system.model.BorrowRecord;
import com.ashish.library_management_system.model.Borrower;
import com.ashish.library_management_system.service.BorrowerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
public class BorrowerController {

    private final BorrowerService borrowerService;


    @PostMapping("/borrowers")
    public ResponseEntity<String> registerBorrower(@RequestBody Borrower borrower) {
        log.info("Registering new borrower: {}", borrower.getName());
        borrowerService.registerBorrower(borrower);
        return ResponseEntity.status(HttpStatus.CREATED).body("Borrower registered successfully");
    }


    @PostMapping("/borrow")
    public ResponseEntity<String> borrowBook(@RequestBody BorrowRecord record) throws BorrowerNotFoundException {
        log.info("Borrower {} attempting to borrow book {}", record.getBorrowerId(), record.getBookId());

        boolean success = borrowerService.borrowABook(record);

        if (success) {
            return ResponseEntity.ok("Book borrowed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Book is not available or borrowing failed");
        }
    }


    @GetMapping("/borrowers/{id}/records")
    public ResponseEntity<List<BorrowRecord>> getBorrowerHistory(@PathVariable("id") Long borrowerId) {
        log.info("Fetching borrow history for borrower {}", borrowerId);
        List<BorrowRecord> records = borrowerService.getAllBorrowerHistory(borrowerId);
        return ResponseEntity.ok(records);
    }


    @GetMapping("/borrowers/overdue")
    public ResponseEntity<List<Borrower>> getBorrowersWithOverdue() {
        log.info("Fetching borrowers with overdue books");
        List<Borrower> borrowers = borrowerService.getBorrowersWithDueDate();
        return ResponseEntity.ok(borrowers);
    }


    @PostMapping("/return/{borrowerId}/{bookId}")
    public ResponseEntity<String> returnBook(@PathVariable Long borrowerId,
                                             @PathVariable Long bookId) {
        log.info("Borrower {} returning book {}", borrowerId, bookId);
        borrowerService.returnBook(bookId, borrowerId);
        return ResponseEntity.ok("Book returned successfully");
    }

    @GetMapping("/records/active")
    public ResponseEntity<List<BorrowerBooksDTO>> getBorrowersWithBooks() {
        log.info("Request received: Fetching borrowers with borrowed books");

        List<BorrowerBooksDTO> borrowers = borrowerService.getBorrowersWithBooks();

        if (borrowers.isEmpty()) {
            log.warn("No borrower records found");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        log.info("Successfully fetched {} borrower records", borrowers.size());
        return ResponseEntity.ok(borrowers);
    }
}
