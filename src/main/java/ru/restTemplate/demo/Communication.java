package ru.restTemplate.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.restTemplate.demo.model.User;

import java.util.Collections;

@Component
public class Communication {

    @Autowired
    private RestTemplate restTemplate;
    private static final String BASE_URL = "http://94.198.50.185:7081/api/users";
    private static String sessionId;
    private final HttpHeaders headers = new HttpHeaders();
    static String result = "";

    public void getAllUsers() {
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(BASE_URL, HttpMethod.GET, null,
                        new ParameterizedTypeReference<String>() {
                        });
        String setCookieHeader = responseEntity.getHeaders().getFirst("Set-Cookie");
        if (setCookieHeader != null) {
            sessionId = setCookieHeader.split(";")[0];
            System.out.println("Полученный sessionId: " + sessionId);
        } else {
            System.out.println("Session ID не найден в заголовках ответа.");
        }
    }

    public void saveUser() {
        headers.put(HttpHeaders.COOKIE, Collections.singletonList(sessionId));
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        User user = new User("James", "Brown", (byte) 27);
        user.setId(3L);
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_URL, HttpMethod.POST, entity, String.class);
        result = result + responseEntity.getBody();
    }

    public void updateUser() {
        User user = new User("Thomas", "Shelby", (byte) 29);
        user.setId(3L);
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        String response = restTemplate.exchange(BASE_URL, HttpMethod.PUT, entity, String.class).getBody();
        result = result + response;
        new ResponseEntity<>(response, HttpStatus.OK);
    }

    public String deleteUser(Long id) {
        HttpEntity<User> entity = new HttpEntity<>(headers);
        String request = restTemplate.exchange(BASE_URL + "/" + id, HttpMethod.DELETE, entity, String.class).getBody();
        result = result + request;
        new ResponseEntity<>(request, HttpStatus.OK);
        return result;
    }

    public String finalApp() {
        if (result.length() == 18) {
            return "Mission completed! - true";
        } else {
            return "Mission completed! - false";
        }
    }
}
