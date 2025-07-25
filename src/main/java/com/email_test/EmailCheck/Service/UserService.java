package com.email_test.EmailCheck.Service;

import com.email_test.EmailCheck.Dto.UserDto;
import com.email_test.EmailCheck.Modal.User;
import com.email_test.EmailCheck.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.WatchService;

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

    public User createUser(UserDto req) {
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
        return userRepository.save(user);
    }

    public User createUserViaGoogle(UserDto req){
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
        return userRepository.save(user);
    }


    public User getUserFromEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserByJwt() throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        if(user == null)
                throw  new Exception("User Not found");

        return user;
    }
}
