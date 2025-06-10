package wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wallet.dto.CreateUserRequestDTO;
import wallet.dto.userDTO;
import wallet.service.AccountService;
import wallet.service.KeycloakUserService;

@RestController
@RequestMapping("/wallet/users")
public class UserController {

    @Autowired
    private KeycloakUserService userService;

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody CreateUserRequestDTO request) {
        userService.createUser(
                request.userName,
                request.email,
                request.firstName,
                request.lastName,
                request.password
        );

        userDTO userAccount = userService.getUserByUsername(request.userName);
        accountService.createAccount(userAccount.getId());

        return ResponseEntity.ok("Usuário criado com sucesso!");
    }
}