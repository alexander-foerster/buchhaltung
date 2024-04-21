package de.foersterdigitalbusiness.buchhaltung.accout;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import de.foersterdigitalbusiness.buchhaltung.bookings.BuchungenView;
import de.foersterdigitalbusiness.buchhaltung.period.Period;
import de.foersterdigitalbusiness.buchhaltung.period.PeriodRepository;
import de.foersterdigitalbusiness.buchhaltung.security.AuthenticatedUser;
import de.foersterdigitalbusiness.buchhaltung.user.User;
import de.foersterdigitalbusiness.buchhaltung.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@PageTitle("Konten")
@Route(value = "accounts", layout = MainLayout.class)
@RolesAllowed("USER")
public class KontenView extends VerticalLayout implements HasUrlParameter<Long> {

    private AuthenticatedUser authenticatedUser;
    private AccountRepository accountRepository;
    private PeriodRepository periodRepository;
    private final Grid<Account> grid = new Grid<>(Account.class, false);
    private List<Account> accounts;
    private Long periodIdParam;
    private Period period;

    public KontenView(AuthenticatedUser authenticatedUser, AccountRepository accountRepository, PeriodRepository periodRepository) {
        this.authenticatedUser = authenticatedUser;
        this.accountRepository = accountRepository;
        this.periodRepository = periodRepository;

        addClassName("konten-view");
        setSizeFull();

        grid.addColumn("number")
                .setHeader("Kontonummer").setAutoWidth(true);
        grid.addColumn("accountType")
                .setHeader("Typ").setAutoWidth(true);
        grid.addColumn("ebWert")
                .setHeader("EB-Wert").setAutoWidth(true);
        grid.addColumn("sbWert")
                .setHeader("SB-Wert").setAutoWidth(true);
        grid.addComponentColumn(item -> {
            Button btn = new Button("Buchungen");
            btn.addClickListener( e -> {
                btn.getUI().ifPresent( ui -> ui.navigate(BuchungenView.class, new RouteParameters("account", item.getId().toString())));
            });
            return btn;
        });

        add(grid);
        final var newDialog = getNewDialog(periodRepository);

        final var newButton = new Button("Neues Konto anlegen");
        newButton.addClickListener(clickEvent -> {
            newDialog.open();
        });
        add(newButton);
    }

    private Dialog getNewDialog(PeriodRepository periodRepository) {
        final var newDialog = new Dialog();
        final var ktoNummerTextField = new TextField("Kontonummer");
        final var ktoTypCB = new ComboBox<AccountType>();
        ktoTypCB.setItems(AccountType.values());
        final var ebWertTextField = new TextField("EB-Wert");
        final var sbWertTextField = new TextField("SB-Wert");

        Button closeButton = new Button("Speichern", e -> {
            var yearString = ktoNummerTextField.getValue();
            try {
                var ebWert = new BigDecimal(ebWertTextField.getValue());
                var sbWert = new BigDecimal(sbWertTextField.getValue());

                accountRepository.save(new Account(period, ktoNummerTextField.getValue(), ktoTypCB.getValue(), ebWert, sbWert));
                loadData();
            } catch (NumberFormatException exception) {
                final var notification = new Notification();
                notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                notification.show("Ungültige Zahl");
            } catch (Exception exception) {
                final var notification = new Notification();
                notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                notification.show("Datensatz konnte nicht gespeichert werden:\n" +
                        exception.getLocalizedMessage());
            }
            newDialog.close();
            loadData();
        });
        newDialog.add(ktoNummerTextField, ktoTypCB, ebWertTextField, sbWertTextField, closeButton);
        return newDialog;
    }

    private Optional<Period> loadData() {
        final Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            final var user = maybeUser.get();

            if(periodIdParam == null) {
                System.out.println("ID-Parameter == null");
                // Parameter nicht angegeben
                return Optional.empty();
            }
            final var maybePeriod = periodRepository.findById(periodIdParam);
            if (maybePeriod.isPresent()) {
                final var period = maybePeriod.get();
                // Sicherheitscheck: Gehört das Geschäftsjahr wirklich dem User?
                if (period.getUser().getId() != user.getId()) {
                    System.out.println("Geschäftsjahr gehört dem User nicht");
                    return Optional.empty();
                }
                this.period = period;
                accounts = accountRepository.findAllByPeriodId(period.getId());
                grid.setItems(accounts);
            } else
                System.out.println("Geschäftsjahr nicht gefunden");
            return maybePeriod;
        } else
            return Optional.empty();
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long periodId) {
        System.out.println("Parameter: " + periodId);
        this.periodIdParam = periodId;
        loadData();
    }
};
