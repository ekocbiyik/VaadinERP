package com.ekocbiyik.view.sysadminrole;

import com.ekocbiyik.DBDefaultValues;
import com.ekocbiyik.UtilsForSpring;
import com.ekocbiyik.event.DashboardEventBus;
import com.ekocbiyik.model.Company;
import com.ekocbiyik.service.ICompanyService;
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
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by enbiya on 16.12.2016.
 */
public class SysNewCompanyView extends Panel implements View {

    private VerticalLayout root;

    private TextField txtCompanyName;
    private TextField txtEmail;
    private TextField txtPhone;
    private ComboBox cbxLocation;
    private TextArea txtDescription;
    private DateField dfExpireDate;

    /**
     * singleton şuan için olmasın
     */

    public SysNewCompanyView() {

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

        HorizontalLayout newCompanyContent = new HorizontalLayout();
        newCompanyContent.setWidth(100.0f, Unit.PERCENTAGE);
        newCompanyContent.setSpacing(true);
        newCompanyContent.setMargin(true);
        newCompanyContent.addStyleName("profile-form");

        VerticalLayout pict = new VerticalLayout();
        pict.setSizeUndefined();
        pict.setSpacing(true);
        Image profilePict = new Image(null, new ThemeResource("img/profile-pic-300px.jpg"));
        profilePict.setWidth(100.0f, Unit.PIXELS);
        pict.addComponent(profilePict);

        Button upload = new Button("Change", event -> Notification.show("Not Implemented now.."));
        upload.addStyleName(ValoTheme.BUTTON_TINY);
        upload.setWidth(100.0f, Unit.PIXELS);
        pict.addComponent(upload);

        newCompanyContent.addComponent(pict);

        /** ----------- */
        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        newCompanyContent.addComponent(details);
        newCompanyContent.setExpandRatio(details, 1);

        Label section = new Label("Company Info");
        section.addStyleName(ValoTheme.LABEL_H4);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(section);

        txtCompanyName = new TextField("Company Name");
        txtCompanyName.setWidth("100%");
        txtCompanyName.setInputPrompt("Please give Company name");
        details.addComponent(txtCompanyName);

        txtEmail = new TextField("E-mail");
        txtEmail.setWidth("100%");
        txtEmail.setInputPrompt("Please give e-mail");
        details.addComponent(txtEmail);

        txtPhone = new TextField("Phone");
        txtPhone.setWidth("100%");
        txtPhone.setInputPrompt("Please give phone number");
        details.addComponent(txtPhone);

        cbxLocation = new ComboBox("Location");
        cbxLocation.setWidth("30%");
        cbxLocation.setNullSelectionAllowed(false);
        cbxLocation.setInputPrompt("Please specify");
        cbxLocation.addItems(DBDefaultValues.iller);
        cbxLocation.setNewItemsAllowed(false);
        details.addComponent(cbxLocation);

        txtDescription = new TextArea("Description");
        txtDescription.setInputPrompt("Please type some info about the Company...");
        txtDescription.setWidth("50%");
        txtDescription.setHeight("74px");
        details.addComponent(txtDescription);

        dfExpireDate = new DateField("Expire Date");
        dfExpireDate.setWidth("30%");
        dfExpireDate.setValue(new Date());
        dfExpireDate.setRangeStart(new Date());
        details.addComponent(dfExpireDate);

        return newCompanyContent;
    }

    private Component buildFooter() {

        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button ok = new Button("OK");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {

                if (!isFormValuesOK()) {
                    return;
                }

                Company cmp = new Company();
                cmp.setCompanyName(txtCompanyName.getValue());
                cmp.setEmail(txtEmail.getValue());
                cmp.setPhone(txtPhone.getValue());
                cmp.setLocation((String) cbxLocation.getValue());
                cmp.setDescription(txtDescription.getValue());
                cmp.setExpireDate(dfExpireDate.getValue());
                cmp.setCreationDate(new Date());
                cmp.setInActive(true);

                UtilsForSpring.getSingleBeanOfType(ICompanyService.class).save(cmp);

                Notification success = new Notification("Bu kısımda database işlemleri olacak...");
                success.setDelayMsec(2000);
                success.setStyleName("bar success small");
                success.setPosition(Position.BOTTOM_CENTER);
                success.show(Page.getCurrent());

                clearFormFields();

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

        if (!cal.getTime().before(dfExpireDate.getValue())) {
            Notification.show("Expire date must be min. 3 months!", Type.HUMANIZED_MESSAGE);
            return false;

        } else if (txtCompanyName.getValue().length() < 5) {
            Notification.show("Company name must be more than 5 letters! ", Type.HUMANIZED_MESSAGE);
            return false;

        } else if (!txtEmail.getValue().contains(".") || !txtEmail.getValue().contains("@")) {
            Notification.show("e-mail is invalid!", Type.HUMANIZED_MESSAGE);
            return false;

        } else if (txtPhone.getValue().length() < 5) {
            Notification.show("Phone number is invalid!", Type.HUMANIZED_MESSAGE);
            return false;

        } else if ("".equals(cbxLocation.getValue()) || cbxLocation.getValue() == null) {
            Notification.show("Select a location for Company!", Type.HUMANIZED_MESSAGE);
            return false;

        } else if (txtDescription.getValue().length() < 5) {
            Notification.show("Please give a description for Company!", Type.HUMANIZED_MESSAGE);
            return false;
        } else {
            return true;
        }

    }

    private void clearFormFields() {
        txtCompanyName.clear();
        txtPhone.clear();
        txtEmail.clear();
        cbxLocation.clear();
        txtDescription.clear();
    }

    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {
    }
}
