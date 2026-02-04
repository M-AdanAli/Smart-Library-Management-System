package com.adanali.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Objects;

public class BorrowingRecord {

    private final String recordId;
    private final Book borrowedBook;
    private final Borrower borrower;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;
    private BorrowingStatus status;
    private int fine;

    public BorrowingRecord(String recordId, Book borrowedBook, Borrower borrower, LocalDate borrowDate, LocalDate dueDate) {
        this.recordId = recordId;
        this.borrowedBook = borrowedBook;
        this.borrower = borrower;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.fine=0;
        updateStatus();
    }

    public BorrowingRecord(
            @JsonProperty("recordId") String recordId,
            @JsonProperty("borrowedBook") Book borrowedBook,
            @JsonProperty("borrower") Borrower borrower,
            @JsonProperty("borrowDate") LocalDate borrowDate,
            @JsonProperty("dueDate") LocalDate dueDate,
            @JsonProperty("returnDate") LocalDate returnDate,
            @JsonProperty("fine") int fine) {
        this.recordId = recordId;
        this.borrowedBook = borrowedBook;
        this.borrower = borrower;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.fine = fine;
        updateStatus(); // runs with returnDate/fine present
    }

    public String getRecordId() {
        return recordId;
    }

    public Book getBorrowedBook() {
        return borrowedBook;
    }

    public Borrower getBorrower() {
        return borrower;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    @JsonIgnore
    public void setReturnDate(LocalDate returnDate) {
        if (returnDate != null && !returnDate.isBefore(getBorrowDate())){
            this.returnDate = returnDate;
            updateStatus();
        }else throw new IllegalArgumentException("Pass a valid return date!");
    }

    public BorrowingStatus getStatus() {
        return status;
    }

    // Updates the status based on return date and due date.
    public void updateStatus() {
        if (returnDate != null) {
            if (returnDate.isAfter(dueDate)){
                status = BorrowingStatus.OVERDUE;
            }else {
                status = BorrowingStatus.RETURNED;
            }
        } else if (LocalDate.now().isAfter(dueDate)) {
            status = BorrowingStatus.OVERDUE;
        } else {
            status = BorrowingStatus.ACTIVE;
        }
        updateFine();
    }

    public int getFine() {
        return fine;
    }

    @JsonIgnore
    public void setFine(int fine) {
        if (fine >= 0){
            int delta = fine - this.fine;
            if(delta>=0){
                borrower.addPendingFine(delta);
            }else {
                borrower.reducePendingFine(-delta);
            }
            this.fine = fine;
        }else throw new IllegalArgumentException("Fine cannot be negative!");
    }

    public void updateFine() {
        int fineToUpdate = 0;
        if (this.status.equals(BorrowingStatus.OVERDUE)) {
            long daysOverdue;
            if (returnDate != null){
                daysOverdue = dueDate.datesUntil(returnDate).count();
            }else {
                daysOverdue = dueDate.datesUntil(LocalDate.now()).count();
            }
            fineToUpdate = (int) (50 * daysOverdue);
        }
        setFine(fineToUpdate);
    }

    @Override
    public String toString() {
        return String.format("%-50s | %-25s | %-25s | %-20s | %-20s | %-20s | %-10s | %-8s",
                getRecordId(), getBorrowedBook().getTitle(), getBorrower().getName(), getBorrowDate(), getDueDate(), getReturnDate(), getStatus(), getFine() );
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof BorrowingRecord)) {
            return false;
        }
        BorrowingRecord other = (BorrowingRecord) obj;
        return Objects.equals(this.getRecordId(),other.getRecordId());
    }

    @Override
    public int hashCode() {
        return recordId.hashCode();
    }

}
