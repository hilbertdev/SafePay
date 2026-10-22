package com.safepay.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Ring: infrastructure
 * Spring Data generates the implementation at startup - we write no SQL.
 */
public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, String> {
}
