package com.email_test.EmailCheck.Service;
import com.email_test.EmailCheck.Dto.UserDto;
import com.email_test.EmailCheck.Modal.User;
import com.email_test.EmailCheck.Utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AuthService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${app.clientId}")
    private String ci;

    @Value("${app.clientSecret}")
    private String sec;

    public String getAccessToken(String authCode, String redirectUri) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authCode);
        params.add("client_id", ci);
        params.add("client_secret", sec);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://oauth2.googleapis.com/token", request, Map.class
        );

        return (String) response.getBody().get("access_token");
    }

    public Map<String, String> getUserInfoGoogle(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        String url = "https://www.googleapis.com/oauth2/v3/userinfo";

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);

        Map<String, Object> userInfo = response.getBody();

        // Extract username and email
        String name = (String) userInfo.get("name");
        String email = (String) userInfo.get("email");
        String sub = (String) userInfo.get("sub");
        String pic = (String) userInfo.get("picture");

        Map<String, String> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        result.put("subId", sub);
        result.put("pic", pic);

        User user = userService.findByEmail(email);
        if(user == null)
        {
            UserDto req = new UserDto();
            req.setEmail(email);
            req.setUsername(name);
            req.setGoogleId(sub);
            req.setPic(pic);
            User saved = userService.createUserViaGoogle(req);
            wishlistService.createWishlist(saved);
        }

        String jwt = jwtUtils.generateToken(email);
        result.put("jwt", jwt);

        return result;
    }

    public Map<String, String> setUser(String name, String email, String pass) {
        User user = userService.getUserFromEmail(email);
        Map<String, String> result = new HashMap<>();
        if(user != null){
            result.put("status", "user");
        }
        else {
            UserDto req = new UserDto();
            req.setUsername(name);
            req.setEmail(email);
            req.setPass(pass);
            User user1 = userService.createUser(req);
            wishlistService.createWishlist(user1);
            result.put("status", "new_user");
            result.put("name", name);
            result.put("email", email);
            String jwt = jwtUtils.generateToken(email);

            result.put("jwt", jwt);
        }
        return result;
    }

    public Map<String, String> getUserInfo(String email) {
        User user = userService.getUserFromEmail(email);
        Map<String, String> mp = new HashMap<>();
        mp.put("email", email);
        mp.put("name", user.getUsername());
        mp.put("pic", user.getPic());
        String jwt = jwtUtils.generateToken(email);
        mp.put("jwt", jwt);
        mp.put("message", "Success");
        return mp;
    }


//    public String getTopEmailId(String accessToken) {
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//
//        HttpEntity<Void> request = new HttpEntity<>(headers);
//
//        String url = "https://gmail.googleapis.com/gmail/v1/users/me/messages?maxResults=1&q=is:inbox";
//
//        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
//        List<Map<String, String>> messages = (List<Map<String, String>>) response.getBody().get("messages");
//
//        return messages.get(0).get("id");
//    }
//
//    public String getEmailContent(String accessToken, String messageId) {
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//
//        HttpEntity<Void> request = new HttpEntity<>(headers);
//
//        String url = "https://gmail.googleapis.com/gmail/v1/users/me/messages/" + messageId;
//        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
//
//        Map payload = (Map) response.getBody().get("payload");
//        List<Map> parts = (List<Map>) payload.get("parts");
//
//        String bodyData = (String) ((Map) parts.get(0).get("body")).get("data");
//
//        byte[] decodedBytes = Base64.getUrlDecoder().decode(bodyData);
//        return new String(decodedBytes, StandardCharsets.UTF_8);
//    }
}
