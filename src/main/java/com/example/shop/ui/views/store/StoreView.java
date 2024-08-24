package com.example.shop.ui.views.store;

import com.example.shop.ui.views.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "store", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class StoreView extends VerticalLayout {
    public StoreView() {
        add(new H2("Store View"));
    }
}
