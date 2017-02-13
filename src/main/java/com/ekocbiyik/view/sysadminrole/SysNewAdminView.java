package com.ekocbiyik.view.sysadminrole;

import com.ekocbiyik.MainUI;
import com.ekocbiyik.UtilsForSpring;
import com.ekocbiyik.enums.EUserRole;
import com.ekocbiyik.event.DashboardEventBus;
import com.ekocbiyik.model.Company;
import com.ekocbiyik.model.User;
import com.ekocbiyik.service.ICompanyService;
import com.ekocbiyik.service.IUserService;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by enbiya on 16.12.2016.
 */
public class SysNewAdminView extends Panel implements View {


    private VerticalLayout root;
    private HorizontalLayout newAdminContent;
    private HorizontalLayout footer;

    private TextField txtUsername;
    private TextField txtPassword;
    private TextField txtEmail;
    private ComboBox cbxCompanyName;
    private TextField txtFirstName;
    private TextField txtLastName;
    private TextField txtPhone;

    private IUserService userService;
    private ICompanyService companyService;


    public SysNewAdminView() {

        userService = UtilsForSpring.getSingleBeanOfType(IUserService.class);
        companyService = UtilsForSpring.getSingleBeanOfType(ICompanyService.class);

        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);

        root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.addStyleName("dashboard-view");
        setContent(root);
        Responsive.makeResponsive(root);


        Component content = buildContent();
        root.addComponent(content);
        root.setExpandRatio(content, 1);

        root.addComponent(buildFooter());


        /** eğer kayıtlı company yoksa panel disable durumda olmalı */
        if (companyService.getAllCompanies().size() <= 0) {

            newAdminContent.setEnabled(false);
            footer.setEnabled(false);
            Notification.show("Please add new Company firstly!", Type.WARNING_MESSAGE);
        }

    }

    private Component buildContent() {

        newAdminContent = new HorizontalLayout();
        newAdminContent.setWidth(100.0f, Unit.PERCENTAGE);
        newAdminContent.setSpacing(true);
        newAdminContent.setMargin(true);
        newAdminContent.addStyleName("profile-form");

        /** todo burada admin picture company seçildiğinde o company'e ait profil resmi olarak değişsin */
        VerticalLayout pict = new VerticalLayout();
        pict.setSizeUndefined();
        pict.setSpacing(true);
        Image profilePict = new Image(null, new ThemeResource("img/profile-pic-300px.jpg"));
        profilePict.setWidth(100.0f, Unit.PIXELS);
        pict.addComponent(profilePict);

        newAdminContent.addComponent(pict);

        /** ----------- */
        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        newAdminContent.addComponent(details);
        newAdminContent.setExpandRatio(details, 1);

        /** Credentials bilgileri */
        Label credentialsTab = new Label("Credentials");
        credentialsTab.addStyleName(ValoTheme.LABEL_H4);
        credentialsTab.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(credentialsTab);

        txtUsername = new TextField("Username");
        txtUsername.setInputPrompt("Please give username");
        txtUsername.setWidth("100%");
        details.addComponent(txtUsername);

        txtPassword = new TextField("Password");
        txtPassword.setInputPrompt("Please give password");
        txtPassword.setWidth("100%");
        details.addComponent(txtPassword);

        txtEmail = new TextField("E-mail");
        txtEmail.setInputPrompt("Please give e-mail address");
        txtEmail.setWidth("100%");
        details.addComponent(txtEmail);


        /** Admin bilgileri */
        Label section = new Label("Admin Info");
        section.addStyleName(ValoTheme.LABEL_H4);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(section);


        BeanItemContainer<Company> itemContainer = new BeanItemContainer<Company>(Company.class);
        itemContainer.addAll(companyService.getAllCompanies());

        cbxCompanyName = new ComboBox("Company Name", itemContainer);
        cbxCompanyName.setWidth("30%");
        cbxCompanyName.setInputPrompt("Please specify");
        cbxCompanyName.setNullSelectionAllowed(false);
        cbxCompanyName.setItemCaptionPropertyId("companyName");
        cbxCompanyName.setNewItemsAllowed(false);
        details.addComponent(cbxCompanyName);

        txtFirstName = new TextField("First Name");
        txtFirstName.setInputPrompt("Please give first name");
        txtFirstName.setWidth("100%");
        details.addComponent(txtFirstName);

        txtLastName = new TextField("Last Name");
        txtLastName.setInputPrompt("Please give last name");
        txtLastName.setWidth("100%");
        details.addComponent(txtLastName);

        txtPhone = new TextField("Phone");
        txtPhone.setInputPrompt("Please give phone number");
        txtPhone.setWidth("100%");
        details.addComponent(txtPhone);

        return newAdminContent;
    }

    private Component buildFooter() {

        footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button ok = new Button("Save");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {

                if (!isFormValuesOK()) {
                    return;
                }

                User admin = new User();
                admin.setUsername(txtUsername.getValue());
                admin.setPassword(txtPassword.getValue());
                admin.setCompany((Company) cbxCompanyName.getValue());
                admin.setUserRole(EUserRole.ADMIN);/**!!!*/
                admin.setEmail(txtEmail.getValue());
                admin.setFirstName(txtFirstName.getValue());
                admin.setLastName(txtLastName.getValue());
                admin.setPhone(txtPhone.getValue());
                admin.setInActive(true);/**!!!*/
                admin.setCreatedBy(MainUI.getCurrentUser().getFirstName() + " " + MainUI.getCurrentUser().getLastName());
                admin.setCreationDate(new Date());

                UtilsForSpring.getSingleBeanOfType(IUserService.class).save(admin);

                clearFormFields();

                Notification success = new Notification("User has been saved!");
                success.setDelayMsec(2000);
                success.setStyleName("bar success small");
                success.setPosition(Position.BOTTOM_CENTER);
                success.show(Page.getCurrent());
            }
        });

        ok.focus();
        footer.addComponent(ok);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);

        return footer;
    }

    private boolean isFormValuesOK() {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 3);

        if (txtUsername.getValue().length() < 5) {
            Notification.show(" Username must be more than 5 letters!", Type.HUMANIZED_MESSAGE);
            return false;

        } else if (txtPassword.getValue().length() < 5) {
            Notification.show("Password must be more than 5 letters!", Type.HUMANIZED_MESSAGE);
            return false;

        } else if (!txtEmail.getValue().contains("@") || !txtEmail.getValue().contains(".")) {
            Notification.show("Invalid e-mail address!", Type.HUMANIZED_MESSAGE);
            return false;

        } else if ("".equals(cbxCompanyName.getValue()) || cbxCompanyName.getValue() == null) {
            Notification.show("Select a Company for new Admin!", Type.HUMANIZED_MESSAGE);
            return false;

        } else if (txtFirstName.getValue().length() < 5) {
            Notification.show("First Name must be more than 5 letters!", Type.HUMANIZED_MESSAGE);
            return false;

        } else if (txtLastName.getValue().length() < 5) {
            Notification.show("Last Name must be more than 5 letters!", Type.HUMANIZED_MESSAGE);
            return false;

        } else if (txtPhone.getValue().length() < 5 || !txtPhone.getValue().matches("[0-9]+")) {
            Notification.show("Invalid phone number!", Type.HUMANIZED_MESSAGE);
            return false;

        } else {
            return true;
        }
    }

    private void clearFormFields() {

        txtUsername.clear();
        txtPassword.clear();
        cbxCompanyName.clear();
        txtEmail.clear();
        txtFirstName.clear();
        txtLastName.clear();
        txtPhone.clear();
    }

    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {

    }
}
