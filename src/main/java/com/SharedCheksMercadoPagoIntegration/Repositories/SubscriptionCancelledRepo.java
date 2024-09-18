package com.SharedCheksMercadoPagoIntegration.Repositories;

import com.SharedCheksMercadoPagoIntegration.Entities.SubscribeOrderCancelled;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriptionCancelledRepo extends JpaRepository<SubscribeOrderCancelled, UUID> {

    List<SubscribeOrderCancelled> findByEmailProfileID(String email);
}
