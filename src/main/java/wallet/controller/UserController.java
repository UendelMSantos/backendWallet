package wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wallet.dto.CreateUserRequestDTO;
import wallet.service.KeycloakUserService;

@RestController
@RequestMapping("/wallet/users")
public class UserController {

    @Autowired
    private KeycloakUserService userService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody CreateUserRequestDTO request) {
        userService.createUser(
                request.userName,
                request.email,
                request.firstName,
                request.lastName,
                request.password
        );
        return ResponseEntity.ok("Usuário criado com sucesso!");
    }
}