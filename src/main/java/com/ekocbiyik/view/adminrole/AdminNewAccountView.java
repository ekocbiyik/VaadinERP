package com.ekocbiyik.view.adminrole;

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
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
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

import java.util.Date;

/**
 * Created by enbiya on 18.12.2016.
 */
public class AdminNewAccountView extends Panel implements View {

    private VerticalLayout root;
    private HorizontalLayout newAccountContent;
    private TextField txtUsername;
    private TextField txtPassword;
    private TextField txtEmail;
    private ComboBox cbxCompanyName;
    private TextField txtFirstName;
    private TextField txtLastName;
    private TextField txtPhone;
    private HorizontalLayout footer;

    private ICompanyService companyService;

    public AdminNewAccountView() {

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
    }

    private Component buildContent() {

        newAccountContent = new HorizontalLayout();
        newAccountContent.setWidth(100.0f, Unit.PERCENTAGE);
        newAccountContent.setSpacing(true);
        newAccountContent.setMargin(true);
        newAccountContent.addStyleName("profile-form");

        /** todo profil fotoğrafı otomatik seçilsin */
        VerticalLayout pict = new VerticalLayout();
        pict.setSizeUndefined();
        pict.setSpacing(true);
        Image profilePict = new Image(null, new ThemeResource("img/profile-pic-300px.jpg"));
        profilePict.setWidth(100.0f, Unit.PIXELS);
        pict.addComponent(profilePict);

        newAccountContent.addComponent(pict);

        /** ----------- */
        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        newAccountContent.addComponent(details);
        newAccountContent.setExpandRatio(details, 1);

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
        Label section = new Label("Accountant Info");
        section.addStyleName(ValoTheme.LABEL_H4);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(section);


        BeanItemContainer<Company> itemContainer = new BeanItemContainer<Company>(Company.class);
        itemContainer.addAll(companyService.getAllCompanies());

        cbxCompanyName = new ComboBox("Company Name", itemContainer);
        cbxCompanyName.setWidth("30%");
        cbxCompanyName.setEnabled(false);
        cbxCompanyName.setNullSelectionAllowed(false);
        cbxCompanyName.setNewItemsAllowed(false);
        cbxCompanyName.setImmediate(true);
        cbxCompanyName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        cbxCompanyName.setItemCaptionPropertyId("companyName");
        cbxCompanyName.select(cbxCompanyName.getItemIds().toArray()[getCompanyIndex()]);
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

        return newAccountContent;
    }

    private Component buildFooter() {

        footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button ok = new Button("Save Accountant");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {

                if (!isFormValuesOK()) {
                    return;
                }

                User account = new User();
                account.setUsername(txtUsername.getValue());
                account.setPassword(txtPassword.getValue());
                account.setEmail(txtEmail.getValue());
                account.setUserRole(EUserRole.ACCOUNT);/**!!!*/

                if (cbxCompanyName.getValue() == null) {
                    account.setCompany(VaadinSession.getCurrent().getAttribute(User.class).getCompany());
                } else {
                    account.setCompany((Company) cbxCompanyName.getValue());
                }

                account.setFirstName(txtFirstName.getValue());
                account.setLastName(txtLastName.getValue());
                account.setPhone(txtPhone.getValue());
                account.setInActive(true);/**!!!*/
                account.setCreatedBy(MainUI.getCurrentUser().getFirstName() + " " + MainUI.getCurrentUser().getLastName());
                account.setCreationDate(new Date());

                try {

                    UtilsForSpring.getSingleBeanOfType(IUserService.class).save(account);

                    Notification success = new Notification("Accountant has been added successfully!");
                    success.setDelayMsec(2000);
                    success.setStyleName("bar success small");
                    success.setPosition(Position.BOTTOM_CENTER);
                    success.show(Page.getCurrent());

                    clearFormFields();

                } catch (Exception e) {
                    Notification.show("Oops! An error occured..");
                }
            }
        });

        ok.focus();
        footer.addComponent(ok);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);

        return footer;
    }

    private int getCompanyIndex() {

        /** bu metot company combobox set vaue yapamadığımız için geçici çözüm!!*/
        for (int i = 0; i < cbxCompanyName.getItemIds().toArray().length; i++) {
            if (((Company) cbxCompanyName.getItemIds().toArray()[i]).getCompanyID()
                    == VaadinSession.getCurrent().getAttribute(User.class).getCompany().getCompanyID()) {
                return i;
            }
        }

        /** buraya hiç düşmeyeceği için problem yok*/
        return 0;
    }

    private boolean isFormValuesOK() {

        if (txtUsername.getValue().length() < 5) {
            Notification.show("Username must be more than 5 letters!", Type.WARNING_MESSAGE);
            return false;
        } else if (txtPassword.getValue().length() < 5) {
            Notification.show("Password must be more than 5 letters!", Type.WARNING_MESSAGE);
            return false;
        } else if (!txtEmail.getValue().contains(".") || !txtEmail.getValue().contains("@")) {
            Notification.show("e-mail is invalid!", Type.WARNING_MESSAGE);
            return false;
        } else if (txtFirstName.getValue().length() < 3) {
            Notification.show("First name must be more than 3 letters!", Type.WARNING_MESSAGE);
            return false;
        } else if (txtLastName.getValue().length() < 2) {
            Notification.show("Last name must be more than 2 letters!", Type.WARNING_MESSAGE);
            return false;
        } else if (txtPhone.getValue().length() < 5 || !txtPhone.getValue().matches("[0-9]+")) {
            Notification.show("Invalid phone number!", Type.WARNING_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    private void clearFormFields() {

        txtUsername.clear();
        txtPassword.clear();
        txtEmail.clear();
        txtFirstName.clear();
        txtLastName.clear();
        txtPhone.clear();
    }

    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {

    }
}
