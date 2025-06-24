package com.example.AuthService.config;

import com.example.AuthService.entity.Role;
import com.example.AuthService.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Override
    public void run(String... args) throws Exception {
            createRoleIfNonExists("ADMIN");
            createRoleIfNonExists("DRIVER");
            createRoleIfNonExists("OPERATOR");
    }


    private void createRoleIfNonExists(String roleName){
            if (roleRepository.findByRole(roleName).isEmpty()){
                        Role role = new Role();
                        role.setRole(roleName);
                        roleRepository.save(role);
            }
    }


}
