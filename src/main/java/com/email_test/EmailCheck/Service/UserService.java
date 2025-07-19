package com.email_test.EmailCheck.Service;

import com.email_test.EmailCheck.Dto.UserDto;
import com.email_test.EmailCheck.Modal.User;
import com.email_test.EmailCheck.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User findByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }

    public void createUser(UserDto req) {
        String name = req.getUsername();
        String email = req.getEmail();
        String pass = req.getPass();
        String sub = req.getGoogleId();
        String pic = req.getPic();

        User user = new User();
        user.setUsername(name);
        user.setEmail(email);
        user.setGoogleId(sub);
        user.setPic(pic);
        user.setPass(passwordEncoder.encode(pass));
        userRepository.save(user);
    }

    public User getEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
