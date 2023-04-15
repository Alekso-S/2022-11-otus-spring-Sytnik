package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.domain.UserAccount;

public interface UserAccountRepository extends MongoRepository<UserAccount, String> {

    UserAccount findByUsername(String username);
}
