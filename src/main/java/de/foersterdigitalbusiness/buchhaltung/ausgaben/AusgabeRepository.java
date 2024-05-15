package de.foersterdigitalbusiness.buchhaltung.ausgaben;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AusgabeRepository extends JpaRepository<Ausgabe, Long> {
    List<Ausgabe> findAllByOrderByDatumAsc();
}