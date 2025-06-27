package wallet.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import wallet.dto.MoneyDepositDTO;
import wallet.dto.TransactionDTO;
import wallet.dto.TransactionResponseDTO;
import wallet.service.AccountService;
import wallet.service.TransactionService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/wallet/transaction")
public class TransactionController {
    private final TransactionService transactionService;
    private final AccountService accountService;

    public TransactionController(
            TransactionService transactionService,
            AccountService accountService
    ) {
        this.transactionService = transactionService;
        this.accountService = accountService;
    };

    @PostMapping
    public ResponseEntity<String> createTransaction(@RequestBody TransactionDTO transaction) {

        accountService.createTransaction(transaction);

        return ResponseEntity.ok("Transação Realizada com sucesso!");
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody MoneyDepositDTO moneyDepositDTO) {
        accountService.deposit(moneyDepositDTO);
        return ResponseEntity.ok("Deposito Realizado com sucesso!");
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsById(
            @RequestParam("username") String userId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim

    ) {

        List<TransactionResponseDTO> transacoes = transactionService.findTransactionsByUserAndPeriod(userId, inicio, fim);

        if (transacoes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(transacoes);
    }


}
