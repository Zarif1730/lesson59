package com.example.lesson59.loader;

import com.example.lesson59.entity.Role;
import com.example.lesson59.entity.User;
import com.example.lesson59.repository.RoleRepository;
import com.example.lesson59.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    String init;   // update

    @Override
    public void run(String... args) throws Exception {
        try {
            if (init.equals("create")) {
                Role roleAdmin=new Role();
                roleAdmin.setId(1L);
                roleAdmin.setName("ROLE_ADMIN");

                roleRepository.save(roleAdmin);

                Role roleUser=new Role();
                roleUser.setId(2L);
                roleUser.setName("ROLE_USER");

                roleRepository.save(roleUser);

                User user=new User();
                user.setUserName("admin");
                user.setFullName("ADMIN");
                user.setRoleList(Collections.singletonList(roleAdmin));
                user.setPhoneNumber("+998977777777");
                user.setPassword(passwordEncoder.encode("123"));

                userRepository.save(user);
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
