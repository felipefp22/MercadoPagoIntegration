package com.SharedCheksMercadoPagoIntegration.Repositories;

import com.SharedCheksMercadoPagoIntegration.Entities.PremiumOrderPaidAndExpired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PremiumPaidAndExpiredRepo extends JpaRepository<PremiumOrderPaidAndExpired, UUID> {

    List<PremiumOrderPaidAndExpired> findByEmailProfileID(String email);
}
