package de.foersterdigitalbusiness.buchhaltung.period;

import de.foersterdigitalbusiness.buchhaltung.accout.Account;
import de.foersterdigitalbusiness.buchhaltung.data.AbstractEntity;
import de.foersterdigitalbusiness.buchhaltung.user.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "period")
public class Period extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int year;

    @OneToMany(mappedBy = "period")
    private List<Account> accounts = new ArrayList<>();

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Period() {
        super();
    }
    public Period(User user, int year) {
        super();
        this.user = user;
        this.year = year;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Period period = (Period) o;

        if (year != period.year) return false;
        return user.equals(period.user);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + year;
        return result;
    }
}