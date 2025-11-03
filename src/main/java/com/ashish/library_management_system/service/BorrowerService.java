package com.ashish.library_management_system.service;

import com.ashish.library_management_system.dto.BookBowworedDTO;
import com.ashish.library_management_system.dto.BorrowerBooksDTO;
import com.ashish.library_management_system.exception.BorrowerNotFoundException;
import com.ashish.library_management_system.model.Book;
import com.ashish.library_management_system.model.BorrowRecord;
import com.ashish.library_management_system.model.Borrower;
import com.ashish.library_management_system.model.BorrowerSpecification;
import com.ashish.library_management_system.repository.BookRepository;
import com.ashish.library_management_system.repository.BorrowRecordRepository;
import com.ashish.library_management_system.repository.BorrowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BorrowerService {

    @Autowired
    BorrowerRepository borrowerRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookService bookService;

    @Autowired
    BorrowRecordRepository borrowRecordRepository;

    public void registerBorrower(Borrower borrower){
        borrowerRepository.save(borrower);
    }

    public Borrower getBorrowerByid(long id){
        return  borrowerRepository.findById(id).get();
    }

    public  boolean  borrowABook(BorrowRecord record) throws BorrowerNotFoundException {
        synchronized (this){
            if(checkValidityAndUpdateRecord(record)){
                borrowRecordRepository.save(record);
                return true;
            }
            else{
                return false;
            }
        }
    }

    private boolean checkValidityAndUpdateRecord(BorrowRecord record) throws BorrowerNotFoundException {
        if(!isBorrowerExistsInDB(record.getBorrowerId()))throw new BorrowerNotFoundException("Borrower is not registered");
        Book book = bookService.getBookById(record.getBookId());
        Borrower borrower = getBorrowerByid(record.getBorrowerId());
        List<BorrowRecord> borrowRecords = getAllBorrowerHistory(borrower.getId());

        long count = borrowRecords.stream().count();
        if(count>=borrower.getMaxBorrowLimit()){
            return false;
        }

        if(!book.isAvailable())return false;

        book.setAvailableCopies(book.getAvailableCopies()-1);
        bookRepository.save(book);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,14);
        record.setDueDate(calendar.getTime());
        record.setBorrowDate(new Date(System.currentTimeMillis()));

        return true;
    }

    private boolean isBorrowerExistsInDB(long id){
        Specification<Borrower> spec = BorrowerSpecification.isBorrowerExist(id);
        return borrowerRepository.exists(spec);
    }

    public List<BorrowRecord> getAllBorrowerHistory(long id){
        return borrowRecordRepository.findBorrowRecordsById(id);
    }

    public List<Borrower> getBorrowersWithDueDate(){
        List<BorrowRecord> allDueDatesRecords = borrowRecordRepository.findOverDueBorrowerRecords();
        List<Borrower> borrowersList = allDueDatesRecords.stream()
                .map(record->getBorrowerByid(record.getBorrowerId()))
                .toList();
        return borrowersList;
    }



    @Transactional
    public synchronized   float returnBook(long bookId, long borrowerId){
        BorrowRecord borrowRecord= borrowRecordRepository.findBorrowRecordByBookIdAndBorrowerId(bookId,borrowerId);
        Date today = new Date();

        float fineAmount =0.00f;
        if(borrowRecord.getDueDate().before(today)){
            long daysSinceOverdue = (today.getTime() - borrowRecord.getDueDate().getTime())/(1000 * 60 * 60 *24);
            fineAmount = daysSinceOverdue * 10.00f;
            borrowRecord.setFineAmount(fineAmount);
        }
        borrowRecord.setReturnDate(today);
        borrowRecordRepository.save(borrowRecord);
        Book book = bookService.getBookById(borrowRecord.getBookId());
        book.setAvailableCopies(book.getAvailableCopies()+1);
        bookRepository.save(book);
        return  fineAmount;
    }

    public List<BorrowerBooksDTO> getBorrowersWithBooks() {
        List<Object[]> records = borrowRecordRepository.fetchBorrowerBookDetails();

        // Group by borrowerId
        Map<Long, List<BookBowworedDTO>> map = new HashMap<>();

        for (Object[] row : records) {
            Long borrowerId = (Long) row[0];
            String title = (String) row[1];
            Date dueDate = (Date) row[2];

            map.computeIfAbsent(borrowerId, k -> new ArrayList<>())
                    .add(new BookBowworedDTO(title, dueDate));
        }

        // Build final list
        return map.entrySet().stream()
                .map(entry -> {
                    Borrower borrower = borrowerRepository.findById(entry.getKey()).orElse(null);
                    if (borrower == null) return null;
                    BorrowerBooksDTO  borrowerBooksDTO = new BorrowerBooksDTO();
                    borrowerBooksDTO.setBorrowerId(borrower.getId());
                    borrowerBooksDTO.setBorrowerName(borrower.getName());
                    borrowerBooksDTO.setBorrowerEmail(borrower.getEmail());
                    borrowerBooksDTO.setBookdBorrowed(entry.getValue());
                    return borrowerBooksDTO;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


}
