package de.foersterdigitalbusiness.buchhaltung.ausgaben;

import de.foersterdigitalbusiness.buchhaltung.data.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ausgabe")
public class Ausgabe extends AbstractEntity {
    private LocalDate datum;
    private String text;
    private BigDecimal betrag;

    public Ausgabe(LocalDate datum, String text, BigDecimal betrag) {
        this.datum = datum;
        this.text = text;
        this.betrag = betrag;
    }

    public Ausgabe() {
        this.datum = LocalDate.now();
        this.betrag = new BigDecimal(0);
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public BigDecimal getBetrag() {
        return betrag;
    }

    public void setBetrag(BigDecimal betrag) {
        this.betrag = betrag;
    }
}