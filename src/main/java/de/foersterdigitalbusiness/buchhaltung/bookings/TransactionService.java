package de.foersterdigitalbusiness.buchhaltung.bookings;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Optional<Transaction> get(Long id) {
        return transactionRepository.findById(id);
    }

    public Transaction update(Transaction entity) {
        return transactionRepository.save(entity);
    }

    public Page<Transaction> list(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    public Page<Transaction> list(Pageable pageable, Specification<Transaction> filter) {
        return transactionRepository.findAll(filter, pageable);
    }

    public int count() {
        return (int) transactionRepository.count();
    }
}

