package com.SharedCheksMercadoPagoIntegration.Repositories;

import com.SharedCheksMercadoPagoIntegration.Entities.PremiumOrderPaidAndActive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PremiumPaidAndActiveRepo extends JpaRepository<PremiumOrderPaidAndActive, UUID> {

    List<PremiumOrderPaidAndActive> findByEmailProfileID(String email);
}
