package ru.skillbox.repository;
import org.springframework.stereotype.Repository;
import ru.skillbox.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    boolean existsByEmail(String email);
}
