package com.ashish.library_management_system.repository;


import com.ashish.library_management_system.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord , Long> {

    @Query("SELECT r FROM BorrowRecord r WHERE r.borrowerId = :id")
    List<BorrowRecord> findBorrowRecordsById(@Param("id") long id);

    @Query("SELECT r FROM BorrowRecord r WHERE r.dueDate < CURRENT_DATE AND r.returnDate IS NULL")
    List<BorrowRecord> findOverDueBorrowerRecords();

    @Query("SELECT r FROM BorrowRecord r WHERE r.bookId = :bookId AND r.borrowerId=borrowerId")
    BorrowRecord findBorrowRecordByBookIdAndBorrowerId(@Param("bookId") long id,@Param("borrowerId") long borrowerId);

    @Query(" SELECT br.borrowerId, b.title, br.dueDate FROM BorrowRecord br JOIN Book b ON br.bookId = b.id WHERE br.returnDate IS NULL")
    List<Object[]> fetchBorrowerBookDetails();
}
