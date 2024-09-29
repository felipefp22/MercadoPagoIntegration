package com.SharedCheksMercadoPagoIntegration.Repositories;

import com.SharedCheksMercadoPagoIntegration.Entities.PremiumOrderCancelled;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PremiumCancelledRepo extends JpaRepository<PremiumOrderCancelled, UUID> {

    List<PremiumOrderCancelled> findByEmailProfileID(String email);
}
