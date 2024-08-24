package com.example.shop.ui.views.payment;

import com.example.shop.backend.services.TransactionService;
import com.example.shop.ui.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

/**
 * PaymentView class represents the payment view of the application.
 */
@Route(value = "payment", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class PaymentView extends VerticalLayout {

    /**
     * Constructor for PaymentView initializes the view with transaction service and UI components.
     * @param transactionService Service for managing transactions
     */
    public PaymentView(TransactionService transactionService) {
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        H2 title = new H2("Payments");

        // Content section with transaction list view
        Div content = new Div(new TransactionListView(transactionService));
        content.addClassName("payment-content");
        content.setSizeFull();

        // Add selector and content to the payment view
        add(title, createSelector(), content);
    }

    /**
     * Creates a horizontal layout with selector buttons for payment view navigation.
     *
     * @return HorizontalLayout with payment selector buttons
     */
    private HorizontalLayout createSelector() {
        HorizontalLayout selector = new HorizontalLayout();
        selector.addClassName("payment-selector");

        // Payment overview button (selected by default)
        Button overviewButton = new Button("Payment overview");
        overviewButton.addClassName("selected");

        // Payment dispute button (initially unselected)
        Button disputeButton = new Button("Payment dispute");
        disputeButton.addClassName("unselected");

        // Payout methods button (initially unselected)
        Button payoutButton = new Button("Payout methods");
        payoutButton.addClassName("unselected");

        // Add buttons to the selector layout
        selector.add(overviewButton, disputeButton, payoutButton);
        return selector;
    }
}
