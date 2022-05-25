package com.example.electricbillsystem.security;

import com.example.electricbillsystem.model.Customer;
import com.example.electricbillsystem.repository.CustomerRepo;
import com.example.electricbillsystem.security.bruteForce.LoginAttemptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private CustomerRepo customerRepo;
    private LoginAttemptService loginAttemptService;
    private HttpServletRequest request;

    @Autowired
    public UserDetailsServiceImpl(CustomerRepo customerRepo, LoginAttemptService loginAttemptService, HttpServletRequest request) {
        this.customerRepo = customerRepo;
        this.loginAttemptService = loginAttemptService;
        this.request = request;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        String ip = getClientIP();
        if (loginAttemptService.isBlocked(ip)) {
            throw new RuntimeException("blocked");
        }

        Customer customer = customerRepo.findCustomerByLogin(login);
        if (customer == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }
        else {
            log.info("User found in the database: {}", login);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(customer.getRoles().getName()));
        return new User(customer.getLogin(), customer.getPassword(), authorities);
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

}
