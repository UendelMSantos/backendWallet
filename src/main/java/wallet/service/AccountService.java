package wallet.service;

import org.springframework.stereotype.Service;
import wallet.dto.MoneyDepositDTO;
import wallet.dto.TransactionDTO;
import wallet.dto.userDTO;
import wallet.entities.Account;
import wallet.entities.Transaction;
import wallet.repository.AccountRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;


@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final KeycloakUserService keycloakUserService;
    private final TransactionService transactionService;
    private final Random random = new Random();

    public AccountService(
            AccountRepository accountRepository,
            KeycloakUserService keycloakUserService,
            TransactionService transactionService
    ) {
        this.accountRepository = accountRepository;
        this.keycloakUserService = keycloakUserService;
        this.transactionService = transactionService;
    }

    public void deposit(MoneyDepositDTO moneyDepositDTO) {
        userDTO userOwner = keycloakUserService.getUserByUsername(moneyDepositDTO.getUserName());
        Account account = findAccountByUserId(userOwner.getId());
        if (account == null) {
            throw new IllegalArgumentException("Conta não encontrada para o usuário: " + moneyDepositDTO.getUserName());
        }
        Long valorDeposito = moneyDepositDTO.getValue();
        account.setBalance(account.getBalance() + valorDeposito);
        accountRepository.save(account);
    }

    public String createTransaction(TransactionDTO transaction) {
        userDTO senderUser = verifyUser(transaction.getSenderCPF());
        userDTO receiverUser = verifyUser(transaction.getReceiverCPF());

        Account senderAccount = this.findAccountByUserId(senderUser.getId());
        Account receiverAccount = this.findAccountByUserId(receiverUser.getId());

        if (senderAccount == null) {
            throw new IllegalArgumentException("Conta não encontrada para o usuário: " + senderUser.firstName);
        }
        if (receiverAccount == null) {
            throw new IllegalArgumentException("Conta não encontrada para o usuário: " + receiverUser.firstName);
        }

        if (senderAccount.getBalance() < transaction.getValue()) {
            throw new IllegalArgumentException("O remetente não tem valor Suficiente para a transação");
        }

        senderAccount.setBalance(senderAccount.getBalance() - transaction.getValue());
        accountRepository.save(senderAccount);
        receiverAccount.setBalance(receiverAccount.getBalance() + transaction.getValue());
        accountRepository.save(receiverAccount);

        Transaction transactionSave = new Transaction(
                senderAccount.getUserId(),
                receiverAccount.getUserId(),
                transaction.getValue()
        );

        transactionService.saveTransaction(transactionSave);

        return "Transação concluida com sucesso!";
    }

    public userDTO verifyUser(String userCPF){
        userDTO user = keycloakUserService.getUserByUsername(userCPF);
        if(user == null) {
            System.out.println("user is null");
        }
        return user;
    }

    public void createAccount(String userId) {
        Account account = new Account();
        account.setUserId(userId);
        account.setAccountNumber(generateRandomAccountNumber());
        account.setAgency("0001");
        account.setBalance(0L);
        accountRepository.save(account);
    }

    private String generateRandomAccountNumber() {
        int number = 10000000 + random.nextInt(90000000);
        return String.valueOf(number);
    }

    public Account findAccountByUserId(String userId) {
        return accountRepository.findByUserId(userId);
    }
}
