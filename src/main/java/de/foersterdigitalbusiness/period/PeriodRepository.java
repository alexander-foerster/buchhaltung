package de.foersterdigitalbusiness.period;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PeriodRepository extends JpaRepository<Period, Long> {
    @Transactional
    List<Period> findAllByUserIdOrderByYear(Long userId);
}