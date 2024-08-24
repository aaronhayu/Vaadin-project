package com.example.shop.ui.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.Route;

@Route("login")
public class LoginView extends Composite<LoginOverlay> {

    public LoginView() {
        getContent().setOpened(true);
        getContent().setAction("login");
        getContent().setTitle("Shop");
        getContent().setDescription("Login to access Shop Dashboard");
    }
}
