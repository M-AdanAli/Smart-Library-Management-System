package com.adanali.library.service;

import com.adanali.library.exceptions.EntityDuplicationException;
import com.adanali.library.model.Book;
import com.adanali.library.model.Borrower;
import com.adanali.library.model.BorrowingRecord;
import com.adanali.library.model.BorrowingStatus;
import com.adanali.library.repository.BooksRepository;
import com.adanali.library.repository.BorrowingRecordsRepository;
import com.adanali.library.repository.UsersRepository;
import com.adanali.library.util.StringUtil;

import java.time.LocalDate;
import java.util.*;

public class BorrowingService {
    private BorrowingRecordsRepository borrowingRecordsRepository;
    private UsersRepository usersRepository;
    private BooksRepository booksRepository;


    public BorrowingService(UsersRepository usersRepository, BooksRepository booksRepository) {
        this.usersRepository = usersRepository;
        this.booksRepository = booksRepository;
        borrowingRecordsRepository = new BorrowingRecordsRepository(usersRepository,booksRepository);
    }

    public void addRecord(String recordId, Book book, Borrower borrower, LocalDate borrowDate, LocalDate dueDate) throws EntityDuplicationException {
        BorrowingRecord borrowingRecord = new BorrowingRecord(recordId, book, borrower, borrowDate, dueDate);
        if (getRecordById(borrowingRecord.getRecordId()).isEmpty()){
            borrowingRecordsRepository.add(borrowingRecord);
        }else throw new EntityDuplicationException(BorrowingRecord.class, "Record already exists!");
    }

    public Optional<BorrowingRecord> getRecordById(String recordId){
        StringUtil.validateNotNullOrBlank(recordId,"Record ID");
        return borrowingRecordsRepository.getById(recordId);
    }

    public void borrowBook(Borrower borrower , Book book) throws EntityDuplicationException {
        if (borrower.canBorrow()){
            if (book.isAvailableForBorrow()){
                String newId;
                do {
                    newId = System.currentTimeMillis()+ "-" + UUID.randomUUID();
                }while (getRecordById(newId).isPresent());
                addRecord(newId, book, borrower, LocalDate.now(), LocalDate.now().plusDays(borrower.getBorrowDurationInDays()));
                book.decreaseQuantity(1);
                booksRepository.saveBooks();
            }else throw new IllegalStateException("Book is not available for borrowing right now.");
        }else throw new IllegalStateException("Borrower has a pending fine!");
    }

    public void returnBook(Borrower borrower, Book book){
        BorrowingRecord relatedBorrowingRecord = borrowingRecordsRepository.getAll().stream()
                .filter(borrowingRecord -> borrowingRecord.getBorrower().equals(borrower) && borrowingRecord.getBorrowedBook().equals(book) && borrowingRecord.getStatus().equals(BorrowingStatus.ACTIVE))
                .findFirst()
                .orElseThrow(()->new IllegalStateException("This Borrower does not have an active borrowing record for the book : "+book.getTitle()));
        relatedBorrowingRecord.setReturnDate(LocalDate.now());
        book.increaseQuantity(1);
        borrowingRecordsRepository.saveBorrowingRecords();
        booksRepository.saveBooks();
        usersRepository.saveUsers();
    }

    public void payFine(Borrower borrower, int amount){
        if (amount > 0){
            if (borrower.getPendingFine() > 0 ){
                borrower.reducePendingFine(amount);
                usersRepository.saveUsers();
                borrowingRecordsRepository.saveBorrowingRecords();
            }else throw new IllegalStateException("No pending fine");
        }else throw new IllegalArgumentException("Invalid payment amount!");
    }

    public List<BorrowingRecord> getBorrowingsByStatus(BorrowingStatus status){
        return borrowingRecordsRepository.getAll().stream()
                .filter(borrowingRecord -> borrowingRecord.getStatus().equals(status))
                .toList();
    }

    public List<BorrowingRecord> getActiveBorrowings(){
        return getBorrowingsByStatus(BorrowingStatus.ACTIVE);
    }

    public List<BorrowingRecord> getReturnedBorrowings(){
        return getBorrowingsByStatus(BorrowingStatus.RETURNED);
    }

    public List<BorrowingRecord> getOverdueBorrowings(){
        return borrowingRecordsRepository.getAll()
                .stream()
                .filter(borrowingRecord->( borrowingRecord.getReturnDate() == null ) && LocalDate.now().isAfter( borrowingRecord.getDueDate() ) )
                .toList();
    }

    public List<BorrowingRecord> getBorrowingsByBorrower(Borrower borrower){
        return borrowingRecordsRepository.getAll().stream()
                .filter(record->record.getBorrower().equals(borrower))
                .toList();
    }

    public List<BorrowingRecord> getBorrowingsByBook(Book book){
        return borrowingRecordsRepository.getAll().stream()
                .filter(record->record.getBorrowedBook().equals(book))
                .toList();
    }

    public boolean hasActiveBorrowings (Book book){
        if (book != null){
            return borrowingRecordsRepository.getAll().stream()
                    .filter(record->record.getBorrowedBook().equals(book))
                    .anyMatch(record->record.getStatus().equals(BorrowingStatus.ACTIVE));
        }else throw new IllegalArgumentException("Pass valid Book!");
    }

    public List<BorrowingRecord> getAllRecords(){
        return borrowingRecordsRepository.getAll();
    }
}
