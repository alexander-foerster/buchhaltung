package de.foersterdigitalbusiness.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;
import de.foersterdigitalbusiness.user.User;
import de.foersterdigitalbusiness.security.AuthenticatedUser;
import de.foersterdigitalbusiness.user.benutzer.BenutzerView;
import de.foersterdigitalbusiness.bookings.buchungen.BuchungenView;
import de.foersterdigitalbusiness.views.dashboard.DashboardView;
import de.foersterdigitalbusiness.views.export.ExportView;
import de.foersterdigitalbusiness.period.geschäftsjahre.GeschäftsjahreView;
import de.foersterdigitalbusiness.views.import_.ImportView;
import de.foersterdigitalbusiness.category.kategorien.KategorienView;
import de.foersterdigitalbusiness.accout.konten.KontenView;
import java.io.ByteArrayInputStream;
import java.util.Optional;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("Buchhaltung");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        if (accessChecker.hasAccess(DashboardView.class)) {
            nav.addItem(new SideNavItem("Dashboard", DashboardView.class, LineAwesomeIcon.GLOBE_SOLID.create()));

        }
        if (accessChecker.hasAccess(BuchungenView.class)) {
            nav.addItem(new SideNavItem("Buchungen", BuchungenView.class, LineAwesomeIcon.ALIGN_LEFT_SOLID.create()));

        }
        if (accessChecker.hasAccess(GeschäftsjahreView.class)) {
            nav.addItem(new SideNavItem("Geschäftsjahre", GeschäftsjahreView.class,
                    LineAwesomeIcon.CALENDAR_SOLID.create()));

        }
        if (accessChecker.hasAccess(KontenView.class)) {
            nav.addItem(new SideNavItem("Konten", KontenView.class, LineAwesomeIcon.DOLLAR_SIGN_SOLID.create()));

        }
        if (accessChecker.hasAccess(KategorienView.class)) {
            nav.addItem(
                    new SideNavItem("Kategorien", KategorienView.class, LineAwesomeIcon.FOLDER_OPEN_SOLID.create()));

        }
        if (accessChecker.hasAccess(ImportView.class)) {
            nav.addItem(new SideNavItem("Import", ImportView.class, LineAwesomeIcon.FILE_IMPORT_SOLID.create()));

        }
        if (accessChecker.hasAccess(ExportView.class)) {
            nav.addItem(new SideNavItem("Export", ExportView.class, LineAwesomeIcon.FILE_EXPORT_SOLID.create()));

        }
        if (accessChecker.hasAccess(BenutzerView.class)) {
            nav.addItem(new SideNavItem("Benutzer", BenutzerView.class, LineAwesomeIcon.USER_SOLID.create()));

        }

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            Avatar avatar = new Avatar(user.getName());
            StreamResource resource = new StreamResource("profile-pic",
                    () -> new ByteArrayInputStream(user.getProfilePicture()));
            avatar.setImageResource(resource);
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(user.getName());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
            userName.getSubMenu().addItem("Sign out", e -> {
                authenticatedUser.logout();
            });

            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
