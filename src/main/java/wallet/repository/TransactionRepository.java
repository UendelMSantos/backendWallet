package wallet.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wallet.dto.TransactionDTO;
import wallet.entities.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query(value = """
        SELECT * FROM transactions
        WHERE (sender_id = :userId OR receiver_id = :userId)
        AND timestamp >= :inicio AND timestamp < :fim
        """, nativeQuery = true)
    List<Transaction> findBySenderIdOrReceiverIdAndTimestampBetween(
            @Param("userId") String userId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    @Query(value = """
        SELECT * FROM transactions 
        WHERE (sender_id = :userId OR receiver_id = :userId)
        AND timestamp >= :inicio
        """, nativeQuery = true)
    List<Transaction> findBySenderIdOrReceiverIdAndTimestampAfter(
            @Param("userId") String userId,
            @Param("inicio") LocalDateTime inicio
    );

    @Query(value = """
        SELECT * FROM transactions 
        WHERE (sender_id = :userId OR receiver_id = :userId)
        AND timestamp < :fim
        """, nativeQuery = true)
    List<Transaction> findBySenderIdOrReceiverIdAndTimestampBefore(
            @Param("userId") String userId,
            @Param("fim") LocalDateTime fim
    );

    @Query(value = """
        SELECT * FROM transactions 
        WHERE sender_id = :userId OR receiver_id = :userId
        """, nativeQuery = true)
    List<Transaction> findBySenderIdOrReceiverId(
            @Param("userId") String userId
    );
}
