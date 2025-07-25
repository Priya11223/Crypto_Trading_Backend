package com.email_test.EmailCheck.Controller;

import com.email_test.EmailCheck.Service.AuthService;
import com.email_test.EmailCheck.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserService userService;

    @PostMapping("/emails")
    public ResponseEntity<?> loginViaOauth(@RequestBody Map<String, String> payload) {
        String code = payload.get("code");
        String redirectUri = payload.get("redirectUri");

        String accessToken = authService.getAccessToken(code, redirectUri);
        Map<String, String> details = authService.getUserInfoGoogle(accessToken);

        return new ResponseEntity<>(details, HttpStatus.OK);
    }

    @PostMapping("/user/signIn")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> payload)
    {
        String name = payload.get("name");
        String email = payload.get("email");
        String pass = payload.get("password");

        Map<String, String> details = authService.setUser(name, email, pass);
        return new ResponseEntity<>(details, HttpStatus.OK);
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload){
        try{
            String email = payload.get("email");
            String pass = payload.get("password");
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, pass));
            Map<String, String> details = authService.getUserInfo(email);
            return new ResponseEntity<>(details, HttpStatus.OK);
        }
        catch (Exception e){
            Map<String, String> detail = new HashMap<>();
            detail.put("message", "fail - Invalid Credentials");
            return new ResponseEntity<>(detail, HttpStatus.NOT_FOUND);
        }
    }
}