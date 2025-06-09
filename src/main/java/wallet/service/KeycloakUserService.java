package wallet.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wallet.config.KeycloakProperties;
import wallet.dto.User;

import java.util.*;

@Service
public class KeycloakUserService {

    @Autowired
    private KeycloakAuthService authService;

    @Autowired
    private KeycloakProperties props;

    @Autowired
    private AccountService accountService;

    public void createUser(String username, String email, String firstName, String lastName, String password) {
        String token = authService.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("email", email);
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("emailVerified", true);
        user.put("enabled", true);
        user.put("credentials", List.of(Map.of(
                "type", "password",
                "value", password,
                "temporary", false
        )));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(user, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.postForEntity(
                props.getAuthServerUrl() + "/admin/realms/" + props.getRealm() + "/users",
                request,
                String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erro ao criar usuário: " + response.getBody());
        }

        String userId = getUserIdByUsername(username);
        accountService.createAccount(userId);


    }

    public String getUserIdByUsername(String username) {
        String token = authService.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        String url = props.getAuthServerUrl() + "/admin/realms/" + props.getRealm() + "/users?username=" + username;

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<User[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, User[].class);

        if (response.getBody() != null && response.getBody().length > 0) {
            return response.getBody()[0].getId();
        } else {
            throw new RuntimeException("Usuário não encontrado no Keycloak");
        }
    }

}
