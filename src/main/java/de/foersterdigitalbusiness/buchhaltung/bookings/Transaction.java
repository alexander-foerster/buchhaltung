package de.foersterdigitalbusiness.buchhaltung.bookings;

import de.foersterdigitalbusiness.buchhaltung.accout.Account;
import de.foersterdigitalbusiness.buchhaltung.data.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transaction")
public class Transaction extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private LocalDate date;
    private BigDecimal value;
    private String text;

    public Transaction() {
        super();
    }

    public Transaction(Account account) {
        super();
        this.account = account;
    }

    public Transaction(Account account, LocalDate date, BigDecimal value, String text) {
        super();
        this.account = account;
        this.date = date;
        this.value = value;
        this.text = text;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}