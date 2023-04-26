package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount {

    @Id
    private String id;
    private String username;
    private String password;
    private List<String> roles;

    public UserAccount(String username, String password, List<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
}
