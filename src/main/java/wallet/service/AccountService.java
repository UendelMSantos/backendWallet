package wallet.service;

import org.springframework.stereotype.Service;
import wallet.entities.Account;
import wallet.repository.AccountRepository;

import java.time.LocalDateTime;
import java.util.Random;


@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final Random random = new Random();

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
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
}
