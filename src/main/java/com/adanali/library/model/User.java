package com.adanali.library.model;

import com.adanali.library.util.StringUtil;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Objects;

/**
 * Abstract base class representing a user in the library system.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "role"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Student.class , name = "student"),
        @JsonSubTypes.Type(value = Librarian.class , name = "librarian")
})
public abstract class User {
    private String name;
    private String email;
    private String password;
    private Class role;

    protected User(String name, String email, String password, Class role){
        setName(name);
        setEmail(email);
        setPassword(password);
        this.role = role;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        StringUtil.validateNotNullOrBlank(name,"User Name");
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (StringUtil.isValidEmail(email)) {
            this.email = email;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (StringUtil.isValidPassword(password)) {
            this.password = password;
        }
    }

    /**
     * Abstract method to get the user's role.
     */
    public abstract String getRole();

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(this.getEmail(), user.getEmail());
    }

    @Override
    public int hashCode() {
        return this.getEmail().hashCode();
    }

    @Override
    public String toString() {
        return String.format("%-25s | %-25s",getName(),getEmail());
    }
}
