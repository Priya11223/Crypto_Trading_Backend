package com.email_test.EmailCheck.Controller;

import com.email_test.EmailCheck.Modal.User;
import com.email_test.EmailCheck.Service.AuthService;
import com.email_test.EmailCheck.Service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/emails")
    public ResponseEntity<?> loginViaOauth(@RequestBody Map<String, String> payload) {
        String code = payload.get("code");
        String redirectUri = payload.get("redirectUri");

        String accessToken = authService.getAccessToken(code, redirectUri);
        Map<String, String> details = authService.getUserInfo(accessToken);

        return new ResponseEntity<>(details, HttpStatus.OK);
    }

    @PostMapping("/create/user")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> payload)
    {
        String name = payload.get("name");
        String email = payload.get("email");
        String pass = payload.get("password");

        Map<String, String> details = authService.setUser(name, email, pass);
        return new ResponseEntity<>(details, HttpStatus.CREATED);
    }
}