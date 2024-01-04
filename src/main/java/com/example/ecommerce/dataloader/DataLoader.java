package com.example.ecommerce.dataloader;

import com.example.ecommerce.model.Role;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repo.RoleRespository;
import com.example.ecommerce.repo.UserRepo;
import com.example.ecommerce.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RoleRespository roleRespository;

    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;
    @Override
    public void run(String... args) throws Exception {
        Role r = new Role();
        r.setName("ADMIN");
        roleRespository.save(r);
        Role r2 = new Role();
        r2.setName("USER");
        roleRespository.save(r2);

        User u = new User();
        u.setPassword(passwordEncoder.encode("Abc123"));
        u.setEmail("admin@gmail.com");
        u.setName("admin");
        u.setPhone("0911223344");
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRespository.getRoleByName("ADMIN");
        roles.add(userRole);
        userRepo.save(u);
        u.setRoles(roles);
        userRepo.save(u);
    }
}
