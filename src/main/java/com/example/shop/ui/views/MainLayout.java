package com.example.shop.ui.views;

import com.example.shop.ui.views.dashboard.DashboardView;
import com.example.shop.ui.views.payment.PaymentView;
import com.example.shop.ui.views.store.StoreView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;

/**
 * MainLayout class represents the application's main layout with a sidebar and navigation links.
 */
@CssImport("./styles/sidebar-styles.css")
public class MainLayout extends AppLayout implements RouterLayout, BeforeEnterObserver {

    private final VerticalLayout sidebar;

    /**
     * Constructor for MainLayout initializes the sidebar and adds navigation links.
     */
    public MainLayout() {
        this.sidebar = createSidebar();
        addToDrawer(sidebar);
    }

    /**
     * Creates the sidebar layout with logo, navigation links, and bottom section.
     *
     * @return VerticalLayout representing the sidebar
     */
    private VerticalLayout createSidebar() {
        VerticalLayout sidebar = new VerticalLayout();
        sidebar.addClassName("sidebar");

        // Logo
//        Image logo = new Image("images/logo.png", "Logo");
//        logo.addClassName("logo");
//        sidebar.add(logo);

        // Add navigation links
        sidebar.add(createNavigationLink("Dashboard", VaadinIcon.HOME, DashboardView.class));
        sidebar.add(createNavigationLink("Store", VaadinIcon.CART, StoreView.class));
        sidebar.add(createNavigationLink("Payment", VaadinIcon.CREDIT_CARD, PaymentView.class));

        return sidebar;
    }

    /**
     * Creates a RouterLink for navigation in the sidebar.
     *
     * @param text             Text to display for the navigation link
     * @param icon             Icon to display next to the text
     * @param navigationTarget Class representing the target view component
     * @return RouterLink instance for navigation
     */
    private RouterLink createNavigationLink(String text,
              VaadinIcon icon, Class<? extends Component> navigationTarget) {

        RouterLink link = new RouterLink();
        link.setRoute(navigationTarget);

        // Icon for the navigation link
        Icon vaadinIcon = new Icon(icon);
        vaadinIcon.addClassName("nav-icon");

        // Text label for the navigation link
        Span linkText = new Span(text);
        linkText.addClassName("nav-link-text");

        // Wrapper div for the icon and text
        Div wrapper = new Div(vaadinIcon, linkText);
        wrapper.addClassName("nav-link-wrapper");

        // Add components to the link and set CSS class
        link.add(wrapper);
        link.addClassName("nav-link");
        return link;
    }

    /**
     * Executed before navigation to update active state of navigation links based on current URL path.
     * @param event BeforeEnterEvent triggering before navigation
     */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String path = event.getLocation().getPath();

        // Iterate through sidebar components to find navigation links
        sidebar.getChildren()
                .filter(component -> component instanceof RouterLink)
                .forEach(component -> {
                    RouterLink link = (RouterLink) component;
                    boolean isActive = link.getHref().equals(path);

                    // Set base CSS class for navigation link
                    link.setClassName("nav-link");

                    // Add active CSS class if the link's href matches the current path
                    if (isActive) {
                        link.addClassName("nav-link-active");
                    }
                });
    }
}
