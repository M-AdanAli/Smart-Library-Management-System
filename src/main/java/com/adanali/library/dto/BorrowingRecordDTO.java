package com.adanali.library.dto;

import com.adanali.library.model.BorrowingStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class BorrowingRecordDTO {
    private final String recordId;
    private final String bookIsbn;
    private final String borrowerEmail;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;
    private final LocalDate returnDate;
    private final int fine;
    private final BorrowingStatus status;

    @JsonCreator
    public BorrowingRecordDTO(
            @JsonProperty("recordId") String recordId,
            @JsonProperty("bookIsbn") String bookIsbn,
            @JsonProperty("borrowerEmail") String borrowerEmail,
            @JsonProperty("borrowDate") LocalDate borrowDate,
            @JsonProperty("dueDate") LocalDate dueDate,
            @JsonProperty("returnDate") LocalDate returnDate,
            @JsonProperty("fine") int fine,
            @JsonProperty("status") BorrowingStatus status) {
        this.recordId = recordId;
        this.bookIsbn = bookIsbn;
        this.borrowerEmail = borrowerEmail;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.fine = fine;
        this.status = status;
    }

    public String getRecordId() { return recordId; }
    public String getBookIsbn() { return bookIsbn; }
    public String getBorrowerEmail() { return borrowerEmail; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public int getFine() { return fine; }
    public BorrowingStatus getStatus() { return status; }
}