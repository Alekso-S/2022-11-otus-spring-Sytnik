package ru.otus.spring.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
@Data
public class UserAccount {

    @Id
    private String id;
    private String username;
    private String password;
    private String[] roles;

    public UserAccount(String username, String password, String[] roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
}
