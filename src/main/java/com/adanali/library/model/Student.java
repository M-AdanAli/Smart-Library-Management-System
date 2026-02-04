package com.adanali.library.model;

import com.adanali.library.util.StringUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Represents a student user in the library system.
 */
@JsonTypeName("student")
public class Student extends User implements Borrower{
    private int pendingFine;
    private String address;

    @JsonCreator
    public Student(@JsonProperty("name") String name,
                   @JsonProperty("email") String email,
                   @JsonProperty("password") String password,
                   @JsonProperty("address") String address) {
        super(name, email, password, Student.class);
        setAddress(address);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        StringUtil.validateNotNullOrBlank(address,"Address");
        this.address = address;
    }

    @Override
    public int getPendingFine() {
        return pendingFine;
    }

    @Override
    public void addPendingFine(int fine) {
        if (fine >= 0) {
            this.pendingFine += fine;
        } else throw new IllegalArgumentException("Fine cannot be negative!");
    }

    @Override
    public void reducePendingFine(int fine) {
        if (fine >= 0) {
            if (fine <= this.pendingFine) {
                this.pendingFine -= fine;
            } else {
                this.pendingFine = 0;
                System.out.println("Pending fine is less.");
                System.out.printf("Following amount should be returned to the user : %d",fine-pendingFine);
            }
        } else throw new IllegalArgumentException("Fine cannot be negative!");
    }

    @Override
    public byte getBorrowDurationInDays() {
        return 3;
    }

    @Override
    public String getRole() {
        return "Student";
    }

    @Override
    public String toString() {
        return String.format("%-15s | %-25s | %-20s | %-12s",getName(),getEmail(),getAddress(),getPendingFine());
    }
}
