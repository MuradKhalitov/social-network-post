package ru.skillbox.repository;
import org.springframework.stereotype.Repository;
import ru.skillbox.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findById(UUID uuid);
    Optional<Account> findByEmail(String email);
    boolean existsByEmail(String email);

}
