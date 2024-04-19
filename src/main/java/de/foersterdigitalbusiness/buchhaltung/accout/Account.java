package de.foersterdigitalbusiness.buchhaltung.accout;

import de.foersterdigitalbusiness.buchhaltung.bookings.Transaction;
import de.foersterdigitalbusiness.buchhaltung.data.AbstractEntity;
import de.foersterdigitalbusiness.buchhaltung.period.Period;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
public class Account extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "period_id")
    private Period period;

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions = new ArrayList<>();

    public Period getPeriod() {
        return period;
    }
    public void setPeriod(Period period) {
        this.period = period;
    }

    private String number;
    private AccountType accountType;
    private BigDecimal ebWert;
    private BigDecimal sbWert;

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Account() {
        super();
    }
    public Account(Period period, String number, AccountType accountType, BigDecimal ebWert, BigDecimal sbWert) {
        super();
        this.period = period;
        this.number = number;
        this.accountType = accountType;
        this.ebWert = ebWert;
        this.sbWert = sbWert;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getEbWert() {
        return ebWert;
    }

    public void setEbWert(BigDecimal ebWert) {
        this.ebWert = ebWert;
    }

    public BigDecimal getSbWert() {
        return sbWert;
    }

    public void setSbWert(BigDecimal sbWert) {
        this.sbWert = sbWert;
    }
}