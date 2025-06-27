package wallet.service;

import org.springframework.stereotype.Service;
import wallet.dto.TransactionDTO;
import wallet.dto.TransactionResponseDTO;
import wallet.dto.userDTO;
import wallet.entities.Transaction;
import wallet.repository.TransactionRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {
    private final KeycloakUserService keycloakUserService;
    private TransactionRepository repository;


    public TransactionService(
            TransactionRepository repository,
            KeycloakUserService keycloakUserService) {
        this.repository = repository;
        this.keycloakUserService = keycloakUserService;
    }

    public void saveTransaction(Transaction transaction) {
       repository.save(transaction);
    }

    public List<TransactionResponseDTO> findTransactionsByUserAndPeriod(String userName, LocalDate inicio, LocalDate fim) {
        LocalDateTime inicioDateTime = (inicio != null) ? inicio.atStartOfDay() : null;
        LocalDateTime fimDateTime = (fim != null) ? fim.plusDays(1).atStartOfDay() : null;

        userDTO userTransaction = keycloakUserService.getUserByUsername(userName);

        List<Transaction> transactionsResponse;

        if (inicioDateTime == null && fimDateTime == null) {
            transactionsResponse = repository.findBySenderIdOrReceiverId(userTransaction.getId());
        } else if (inicioDateTime != null && fimDateTime != null) {
            transactionsResponse = repository.findBySenderIdOrReceiverIdAndTimestampBetween(
                    userTransaction.getId(), inicioDateTime, fimDateTime);
        } else if (inicioDateTime != null) {
            transactionsResponse = repository.findBySenderIdOrReceiverIdAndTimestampAfter(
                    userTransaction.getId(), inicioDateTime);
        } else {
            transactionsResponse = repository.findBySenderIdOrReceiverIdAndTimestampBefore(
                    userTransaction.getId(), fimDateTime);
        }

        List<TransactionResponseDTO> transactionDTOs = this.convertTransactionsToDTO(transactionsResponse);
        return transactionDTOs;
    }

    public List<TransactionResponseDTO> convertTransactionsToDTO(List<Transaction> transactions) {
        List<TransactionResponseDTO> transactionDTOs = new ArrayList<>();
        for (Transaction transaction : transactions) {
            String senderName = keycloakUserService.getFirstNameById(UUID.fromString(transaction.getSenderId()));
            String receiverName = keycloakUserService.getFirstNameById(UUID.fromString(transaction.getReceiverId()));
            TransactionResponseDTO dto = new TransactionResponseDTO(
                    transaction.getId(),
                    transaction.getSenderId(),
                    transaction.getReceiverId(),
                    transaction.getValue(),
                    senderName,
                    receiverName,
                    transaction.getTimestamp()
            );
            transactionDTOs.add(dto);
        }
        return transactionDTOs;
    }

}
