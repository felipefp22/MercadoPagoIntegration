package com.SharedCheksMercadoPagoIntegration.Repositories;

import com.SharedCheksMercadoPagoIntegration.Entities.SubscribeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriptionPaidRepo extends JpaRepository<SubscribeOrder, UUID> {

    List<SubscribeOrder> findByEmailProfileID(String email);
}
