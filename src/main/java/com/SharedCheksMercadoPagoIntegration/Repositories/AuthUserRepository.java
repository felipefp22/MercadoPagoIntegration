package com.SharedCheksMercadoPagoIntegration.Repositories;

import com.SharedCheksMercadoPagoIntegration.Entities.AtuhUser.AuthUserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUserLogin, String>{

    boolean existsByUsername(String username);

    Optional<AuthUserLogin> findByUsernameIgnoreCase(String username);

    Optional<List<AuthUserLogin>> findByEmailContainingIgnoreCase(String searchString);

    Optional<List<AuthUserLogin>> findByUsernameContainingIgnoreCase(String searchString);

    Optional<List<AuthUserLogin>> findByUserDatas_CrmContainingIgnoreCase(String searchString);

}
