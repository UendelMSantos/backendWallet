package wallet.service;

import org.springframework.stereotype.Service;
import wallet.dto.MoneyDepositDTO;
import wallet.dto.TransactionDTO;
import wallet.dto.userDTO;
import wallet.entities.Account;
import wallet.repository.AccountRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;


@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final KeycloakUserService keycloakUserService;
    private final Random random = new Random();

    public AccountService(
            AccountRepository accountRepository,
            KeycloakUserService keycloakUserService
    ) {
        this.accountRepository = accountRepository;
        this.keycloakUserService = keycloakUserService;
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
