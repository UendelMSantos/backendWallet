package wallet.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wallet.config.KeycloakProperties;
import wallet.dto.userDTO;

import java.util.*;

@Service
public class KeycloakUserService {

    @Autowired
    private KeycloakAuthService authService;

    @Autowired
    private KeycloakProperties props;

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

    }

    public userDTO getUserByUsername(String username) {
        String token = authService.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        String url = props.getAuthServerUrl() + "/admin/realms/" + props.getRealm() + "/users?username=" + username + "&exact=true";

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<userDTO[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, userDTO[].class);

        if (response.getBody() != null) {
            return response.getBody()[0];
        } else {
            throw new RuntimeException("Usuário não encontrado no Keycloak");
        }
    }

    public String getFirstNameById(UUID id) {
        String token = authService.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        String url = props.getAuthServerUrl() + "/admin/realms/" + props.getRealm() + "/users/" + id;

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<userDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, userDTO.class);

        if (response != null && response.getBody() != null) {
            return response.getBody().getFirstName();
        } else {
            throw new RuntimeException("Usuário não encontrado no Keycloak");
        }
    }

}
