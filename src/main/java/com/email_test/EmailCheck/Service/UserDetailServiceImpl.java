package com.email_test.EmailCheck.Service;

import com.email_test.EmailCheck.Modal.User;
import com.email_test.EmailCheck.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository uRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        User user = uRepo.findByEmail(email);
        if(user != null)
        {
            List<GrantedAuthority> authorityList = new ArrayList<>();
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPass(),
                    authorityList
            );
        }
        throw new UsernameNotFoundException("Email Not Found "+email);
    }
}
