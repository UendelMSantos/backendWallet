package wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wallet.entities.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByUserId(String userId);
    Optional<Account> findByAccountNumber(String accountNumber);
}
