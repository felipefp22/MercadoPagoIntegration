package com.SharedCheksMercadoPagoIntegration.Repositories;

import com.SharedCheksMercadoPagoIntegration.Entities.SubscribeOrderPendind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriptionPendentRepo extends JpaRepository<SubscribeOrderPendind, UUID> {

    List<SubscribeOrderPendind> findByEmailProfileID(String email);
}
