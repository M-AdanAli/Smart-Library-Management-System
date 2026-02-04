package com.adanali.library.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "role"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Student.class , name = "student")
})
public interface Borrower {
    int getPendingFine();
    void addPendingFine(int fine);
    void reducePendingFine(int fine);
    default boolean canBorrow(){
        return getPendingFine()==0;
    }
    byte getBorrowDurationInDays();
    public String getName();
}
