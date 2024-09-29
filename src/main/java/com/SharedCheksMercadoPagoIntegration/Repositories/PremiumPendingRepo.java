package com.SharedCheksMercadoPagoIntegration.Repositories;

import com.SharedCheksMercadoPagoIntegration.Entities.PremiumOrderPendind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PremiumPendingRepo extends JpaRepository<PremiumOrderPendind, UUID> {

    List<PremiumOrderPendind> findByEmailProfileID(String email);
}
