package com.example.ecommerce.repo;

import com.example.ecommerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRespository extends JpaRepository<Role, Integer> {
    @Query("SELECT u FROM Role u WHERE u.name = ?1")
    public Role getRoleByName(String name);
}
