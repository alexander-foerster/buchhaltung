package de.foersterdigitalbusiness.buchhaltung.period;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PeriodRepository extends JpaRepository<Period, Long> {
    @Transactional
    List<Period> findAllByUserIdOrderByYear(Long userId);
    Optional<Period> findById(Long periodId);
}