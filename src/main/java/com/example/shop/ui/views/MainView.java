package com.example.shop.ui.views;

import com.example.shop.ui.views.payment.PaymentView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class MainView extends VerticalLayout implements AfterNavigationObserver {
    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        getUI().ifPresent(ui -> ui.navigate(PaymentView.class));
    }
}
