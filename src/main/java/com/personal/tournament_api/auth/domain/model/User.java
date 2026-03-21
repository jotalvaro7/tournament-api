package com.personal.tournament_api.auth.domain.model;

import com.personal.tournament_api.auth.domain.enums.UserRole;
import com.personal.tournament_api.auth.domain.model.vo.Email;
import com.personal.tournament_api.auth.domain.model.vo.HashedPassword;

import java.util.Objects;

public class User {

    private final Long id;
    private final Email email;
    private final String username;
    private final HashedPassword password;
    private final UserRole role;

    private User(Long id, Email email, String username, HashedPassword password, UserRole role) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // --- Factory Methods ---

    public static User create(String email, String username, String hashedPassword, UserRole role) {
        return new User(null, new Email(email), username, new HashedPassword(hashedPassword), role);
    }

    public static User reconstitute(Long id, String email, String username, String hashedPassword, UserRole role) {
        return new User(id, new Email(email), username, new HashedPassword(hashedPassword), role);
    }

    // --- Getters ---

    public Long getId() { return id; }
    public String getEmail() { return email.value(); }
    public String getUsername() { return username; }
    public String getPassword() { return password.value(); }
    public UserRole getRole() { return role; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return Objects.equals(id, that.id) && Objects.equals(getEmail(), that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getEmail());
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", email='" + getEmail() + "', username='" + username + "', role=" + role + '}';
    }
}