package de.foersterdigitalbusiness.buchhaltung.period.geschäftsjahre;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.foersterdigitalbusiness.buchhaltung.period.Period;
import de.foersterdigitalbusiness.buchhaltung.period.PeriodRepository;
import de.foersterdigitalbusiness.buchhaltung.security.AuthenticatedUser;
import de.foersterdigitalbusiness.buchhaltung.user.User;
import de.foersterdigitalbusiness.buchhaltung.views.MainLayout;

import java.util.List;
import java.util.Optional;

import jakarta.annotation.security.RolesAllowed;

@PageTitle("Geschäftsjahre")
@Route(value = "periods", layout = MainLayout.class)
@RolesAllowed("USER")
public class GeschäftsjahreView extends VerticalLayout {

    PeriodRepository periodRepository;
    private final Grid<Period> grid = new Grid<>(Period.class, false);
    private List<Period> periods;
    private AuthenticatedUser authenticatedUser;


    public GeschäftsjahreView(AuthenticatedUser authenticatedUser, PeriodRepository periodRepository) {
        this.periodRepository = periodRepository;
        this.authenticatedUser = authenticatedUser;

        grid.addColumn("year")
                .setHeader("Geschäftsjahr").setAutoWidth(true);
        grid.addColumn("version")
                .setHeader("Version").setAutoWidth(true);
        grid.addComponentColumn(item -> {
            Button btn = new Button("Open", click -> {
                Notification.show("Show year " + item.getYear());
            });
            return btn;
        });

        var maybeUser = loadData();
        if(maybeUser.isPresent()) {
            grid.setItems(periods);
            add(grid);
            final var newDialog = new Dialog();
            final TextField yearTextField = new TextField("Enter year");
            Button closeButton = new Button("Speichern", e -> {
                var yearString = yearTextField.getValue();
                try {
                    var year = Integer.parseInt(yearString);
                    periodRepository.save(new Period(maybeUser.get(), year));
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
            newDialog.add(yearTextField, closeButton);

            final var newButton = new Button("Neues Geschäftsjahr anlegen");
            newButton.addClickListener(clickEvent -> {
                newDialog.open();
            });
            add(newButton);
        } else
            add(new Span("Internal Error: No User"));
    }

    private Optional<User> loadData() {
        final Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            final var user = maybeUser.get();
            periods = periodRepository.findAllByUserIdOrderByYear(user.getId());
            grid.setItems(periods);
            return Optional.of(user);
        } else {
            final var notification = new Notification();
            notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
            notification.show("Fehler beim Laden der Daten");
            return Optional.empty();
        }
    }
}
