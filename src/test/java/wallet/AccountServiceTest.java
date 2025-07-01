package wallet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import wallet.entities.Account;
import wallet.repository.AccountRepository;
import wallet.service.AccountService;
import wallet.service.KeycloakUserService;
import wallet.service.TransactionService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

	@Mock
	private AccountRepository accountRepository;
	@Mock
	private KeycloakUserService keycloakUserService;
	@Mock
	private TransactionService transactionService;
	@InjectMocks
	private AccountService accountService;

	private Account account;

	@BeforeEach
	void setUp() {

		account = new Account();
	}

	@Test
	void findAccountByUserIdFailTest() {

		var mockId = UUID.randomUUID().toString();

		when(accountRepository.findByUserId(mockId)).thenReturn(Optional.empty());

		assertThrows(Exception.class, () -> accountService.findAccountByUserId(mockId));
	}

	@Test
	void findAccountByUserIdSuccessTest() {

		var mockId = UUID.randomUUID().toString();

		when(accountRepository.findByUserId(mockId)).thenReturn(Optional.of(account));

		assertDoesNotThrow(() -> accountService.findAccountByUserId(mockId));
	}


}

