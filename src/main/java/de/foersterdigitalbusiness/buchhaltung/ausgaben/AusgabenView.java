package de.foersterdigitalbusiness.buchhaltung.ausgaben;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.foersterdigitalbusiness.buchhaltung.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

@PageTitle("Ausgaben")
@Route(value = "ausgaben", layout = MainLayout.class)
@RolesAllowed("USER")
public class AusgabenView extends VerticalLayout {
    private Grid<Ausgabe> grid = new Grid<>(Ausgabe.class, false);
    private List<Ausgabe> ausgaben;
    private Optional<Ausgabe> selectedAusgabe = Optional.empty();
    private AusgabeRepository ausgabeRepository;

    public AusgabenView(AusgabeRepository ausgabeRepository) {
        this.ausgabeRepository = ausgabeRepository;

        H2 heading = new H2("Ausgaben");
        add(heading);

        loadData();

        final var addButton = new Button("Hinzufügen", clickEvent -> {
            getDetailDialog(false).open();
        });
        add(addButton);

        grid.setAllRowsVisible(true);
        grid.addColumn("datum")
                .setHeader("Datum").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn("betrag")
                .setHeader("Betrag").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn("text")
                .setHeader("Text").setAutoWidth(true);
        grid.addComponentColumn(item -> {
            Button btn = new Button("Öffnen");
            btn.addClickListener( e -> {
                selectedAusgabe = Optional.of(item);
                getDetailDialog(true).open();
            });
            return btn;
        }).setFrozenToEnd(true)
                .setAutoWidth(true).setFlexGrow(0);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        grid.addSelectionListener( selectionEvent -> {
            selectedAusgabe = selectionEvent.getFirstSelectedItem();
        });

        add(grid);

        final var deleteButton = new Button("Löschen", clickEvent -> {
            if(selectedAusgabe.isPresent()) {
                try {
                    ausgabeRepository.delete(selectedAusgabe.get());
                    loadData();
                } catch (DataIntegrityViolationException foreignKeyException) {
                    Notification.show(
                            "Ausgabe kann nicht gelöscht werden"
                    );
                }
            }
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        add(deleteButton);
    }

    private void loadData() {
        ausgaben = ausgabeRepository.findAllByOrderByDatumAsc();
        grid.setItems(ausgaben);
    }

    private Dialog getDetailDialog(boolean editMode) {
        final var newDialog = new Dialog();
        newDialog.setHeaderTitle("Neue Ausgabe erfassen");

        final var dateField = new DatePicker("Datum");
        final var nameField = new TextField("Text");
        final var betragField = new TextField("Betrag");

        VerticalLayout dialogLayout = new VerticalLayout(dateField, nameField, betragField);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");
        newDialog.add(dialogLayout);

        Binder<Ausgabe> binder = new Binder<>(Ausgabe.class);
        binder.forField(dateField).bind("datum");
        binder.forField(nameField).asRequired().bind("text");
        binder.forField(betragField)
                .asRequired("Pflichtfeld")
                .withConverter(new StringToBigDecimalConverter("Keine Zahl"))
                .bind("betrag");

        Ausgabe ausgabe;
        if(editMode && selectedAusgabe.isPresent())
            ausgabe = selectedAusgabe.get();
        else ausgabe = new Ausgabe();
        binder.readBean(ausgabe);

        final var cancelButton = new Button("Abbrechen", cancelEvent -> {
            newDialog.close();
            loadData();
        });

        final var saveButton = new Button(editMode ? "Speichern" : "Hinzufügen", saveEvent -> {
            if(ausgabe != null) {
                try {
                    binder.writeBean(ausgabe);
                    ausgabeRepository.save(ausgabe);
                    loadData();
                    newDialog.close();
                } catch (ValidationException e) {
                    // Nichts zu tun, da Validierungsfehler schon an den Eingabefeldern angezeigt werden
                }
            }
        });
        newDialog.getFooter().add(saveButton, cancelButton);
        return newDialog;
    }

}
