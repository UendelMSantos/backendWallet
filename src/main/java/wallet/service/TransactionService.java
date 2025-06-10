package wallet.service;

import org.springframework.stereotype.Service;
import wallet.dto.TransactionDTO;
import wallet.dto.userDTO;
import wallet.entities.Account;
import wallet.repository.TransactionRepository;

@Service
public class TransactionService {
    private TransactionRepository repository;
    private AccountService accountService;
    public TransactionService(
            TransactionRepository repository,
            AccountService accountService
    ) {
        this.repository = repository;
        this.accountService = accountService;
    }

//    public String createTransaction(TransactionDTO transaction) {
//        userDTO senderUser = verifyUser(transaction.getSenderCPF());
//        userDTO receiverUser = verifyUser(transaction.getReceiverCPF());
//
//        Account senderAccount = accountService.findAccountByUserId(senderUser.getId());
//        Account receiverAccount = accountService.findAccountByUserId(receiverUser.getId());
//
//        return null;
//    }
//
//    public userDTO verifyUser(String userCPF){
//        userDTO user = keycloakUserService.getUserByUsername(userCPF);
//        if(user == null) {
//            System.out.println("user is null");
//        }
//        return user;
//    }
}
