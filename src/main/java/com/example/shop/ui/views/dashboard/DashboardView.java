package com.example.shop.ui.views.dashboard;

import com.example.shop.ui.views.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "dashboard", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class DashboardView extends VerticalLayout {
    public DashboardView() {
        add(new H2("Dashboard View"));
    }
}
