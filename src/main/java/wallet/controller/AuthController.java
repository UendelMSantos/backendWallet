package wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wallet.dto.LoginRequestDTO;
import wallet.dto.LoginResponseDTO;
import wallet.service.KeycloakLoginService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private KeycloakLoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO tokens = loginService.login(loginRequest);
        return ResponseEntity.ok(tokens);
    }
}

