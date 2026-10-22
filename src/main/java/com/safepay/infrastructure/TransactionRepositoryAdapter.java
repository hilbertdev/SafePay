package com.safepay.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.safepay.application.TransactionRepository;
import com.safepay.domain.Transaction;

/**
 * Ring: infrastructure
 * This is the ADAPTER - the only class allowed to know both the domain and JPA.
 */
@Repository
public class TransactionRepositoryAdapter implements TransactionRepository {

    private final TransactionJpaRepository jpaRepository;

    public TransactionRepositoryAdapter(TransactionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity saved = jpaRepository.save(toEntity(transaction));
        return toDomain(saved);
    }

    @Override
    public List<Transaction> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Transaction> findById(String id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    private TransactionEntity toEntity(Transaction transaction) {
        return new TransactionEntity(
                transaction.getTransactionId(),
                transaction.getCustomerId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getLocation(),
                transaction.getTimestamp());
    }

    private Transaction toDomain(TransactionEntity entity) {
        return new Transaction(
                entity.getTransactionId(),
                entity.getCustomerId(),
                entity.getAmount(),
                entity.getCurrency(),
                entity.getLocation(),
                entity.getTimestamp());
    }
}
