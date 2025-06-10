package wallet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wallet.dto.MoneyDepositDTO;
import wallet.dto.TransactionDTO;
import wallet.service.AccountService;
import wallet.service.TransactionService;

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
//
//    @PostMapping
//    public ResponseEntity<String> createTransaction(@RequestBody TransactionDTO transaction) {
//
//        transactionService.createTransaction(transaction);
//
//        return ResponseEntity.ok("Transação Realizada com sucesso!");
//    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody MoneyDepositDTO moneyDepositDTO) {
        accountService.deposit(moneyDepositDTO);
        return ResponseEntity.ok("Deposito Realizado com sucesso!");
    }

}
