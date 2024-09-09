package com.SharedCheksMercadoPagoIntegration.Repositories;

import com.SharedCheksMercadoPagoIntegration.Entities.SubscribeOrderPaidAndExpired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriptionPaidAndExpiredRepo extends JpaRepository<SubscribeOrderPaidAndExpired, UUID> {

    List<SubscribeOrderPaidAndExpired> findByEmailProfileID(String email);
}
