package de.foersterdigitalbusiness.buchhaltung.accout;

import de.foersterdigitalbusiness.buchhaltung.period.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Transactional
    List<Account> findAllByPeriodId(Long periodId);
    List<Account> findAll();
}