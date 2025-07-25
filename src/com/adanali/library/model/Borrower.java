package com.adanali.library.model;

public interface Borrower {
    int getPendingFine();
    void addPendingFine(int fine);
    boolean reducePendingFine(int fine);
    default boolean canBorrow(){
        return getPendingFine()==0;
    }
    byte getBorrowDurationInWeeks();
}
