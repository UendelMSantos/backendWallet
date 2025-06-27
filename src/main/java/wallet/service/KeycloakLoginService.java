package wallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wallet.config.KeycloakProperties;
import wallet.dto.LoginRequestDTO;
import wallet.dto.LoginResponseDTO;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class KeycloakLoginService {

    @Autowired
    private KeycloakProperties props;

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", props.getClientId());
        body.add("username", loginRequest.username);
        body.add("password", loginRequest.password);

        // Só se client for confidential:
        if (props.getClientSecret() != null) {
            body.add("client_secret", props.getClientSecret());
        }

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        String tokenUrl = props.getAuthServerUrl() + "/realms/" + props.getRealm() + "/protocol/openid-connect/token";

        ResponseEntity<LoginResponseDTO> response = restTemplate.postForEntity(tokenUrl, request, LoginResponseDTO.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Erro ao autenticar usuário");
        }

        return response.getBody();
    }
}

