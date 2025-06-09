package wallet.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import wallet.entities.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findBySenderIdOrReceiverId(UUID senderId, UUID receiverId);
}
