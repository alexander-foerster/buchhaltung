package de.foersterdigitalbusiness.buchhaltung.bookings;

import de.foersterdigitalbusiness.buchhaltung.data.SamplePerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

}