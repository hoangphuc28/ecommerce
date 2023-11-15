package com.example.ecommerce.service;


import com.example.ecommerce.model.Role;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repo.RoleRespository;
import com.example.ecommerce.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepository;

    @Autowired
    private RoleRespository roleRespository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        User u = userRepository.getUserByEmail(email);
        if (u == null) {
            throw new UsernameNotFoundException("Could not find user");
        }
        Set<GrantedAuthority> authorities = u.getRoles().stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(
                email,
                u.getPassword(),
                authorities
        );
    }
    public Boolean isUserExist(String email) {
        var u = userRepository.getUserByEmail(email);
        return u != null;
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User getUserByString(String s) {
        return  userRepository.getUserByEmail(s);
    }
    public void NewUser(User u) {
            Set<Role> roles = new HashSet<>();
            Role userRole = roleRespository.getRoleByName("USER");
            roles.add(userRole);
            u.setRoles(roles);
            userRepository.save(u);
    }
    public void updateUser(User u) {
        var usr = userRepository.getUserByEmail(u.getEmail());
        usr.setPassword(u.getPassword());
        userRepository.save(usr);
    }
}
