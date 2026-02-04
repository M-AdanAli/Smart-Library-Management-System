package com.adanali.library.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Represents a librarian user in the library system.
 */
@JsonTypeName("librarian")
public class Librarian extends User {

    @JsonCreator
    public Librarian(@JsonProperty("name") String name,
                     @JsonProperty("email") String email,
                     @JsonProperty("password") String password) {
        super(name, email, password, Librarian.class);
    }

    @Override
    public String getRole() {
        return "Librarian";
    }

    // Wil add librarian-specific methods here in the future, if needed.
}
