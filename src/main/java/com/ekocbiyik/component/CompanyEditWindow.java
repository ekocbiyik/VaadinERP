package com.ekocbiyik.component;

import com.ekocbiyik.DBDefaultValues;
import com.ekocbiyik.UtilsForSpring;
import com.ekocbiyik.model.Company;
import com.ekocbiyik.service.ICompanyService;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
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
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by enbiya on 31.12.2016.
 */
public class CompanyEditWindow extends Window {

    private final Company company;
    private Table companyTable;

    private TextField txtCompanyName;
    private TextField txtEmail;
    private TextField txtPhone;
    private ComboBox cbxLocation;
    private TextArea txtDescription;
    private DateField dfExpireDate;
    private DateField dfCreationDate;
    private TextField txtCompanyId;
    private ComboBox cbxActive;

    public CompanyEditWindow(final Company company, Table companyTable) {

        this.company = company;
        this.companyTable = companyTable;


        addStyleName("profile-window");
        Responsive.makeResponsive(this);

        setModal(true);
        setResizable(false);
        setClosable(true);
        setHeight(100.0f, Unit.PERCENTAGE);
        addCloseShortcut(KeyCode.ESCAPE, null);
        setCaption("Edit Company");

        VerticalLayout content = new VerticalLayout();
        content.setHeight("100%");
        content.setMargin(new MarginInfo(false, true, false, true));
        setContent(content);

        Component c = buildContent();
        content.addComponent(c);
        content.setExpandRatio(c, 1f);

        content.addComponent(buildFooter());

    }

    private Component buildContent() {

        HorizontalLayout companyContent = new HorizontalLayout();
        companyContent.setWidth("100%");
        companyContent.setSpacing(true);
        companyContent.setMargin(new MarginInfo(false, true, false, true));
        companyContent.addStyleName("profile-form");

        VerticalLayout pict = new VerticalLayout();
        pict.setSizeUndefined();
        pict.setSpacing(true);
        Image profilePict = new Image(null, new ThemeResource("img/profile-pic-300px.jpg"));
        profilePict.setWidth(100.0f, Unit.PIXELS);
        pict.addComponent(profilePict);

        Button upload = new Button("Change..", event -> Notification.show("Not Implemented"));
        upload.addStyleName(ValoTheme.BUTTON_TINY);
        pict.addComponent(upload);

        companyContent.addComponent(pict);

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        companyContent.addComponent(details);
        companyContent.setExpandRatio(details, 1);

        Label section = new Label("Company Info");
        section.addStyleName(ValoTheme.LABEL_H4);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(section);

        /** id kesinlikle değiştirilemez */
        txtCompanyId = new TextField("Company Id");
        txtCompanyId.setWidth("100%");
        txtCompanyId.setEnabled(false);
        details.addComponent(txtCompanyId);

        txtCompanyName = new TextField("Company Name");
        txtCompanyName.setWidth("100%");
        details.addComponent(txtCompanyName);

        txtEmail = new TextField("E-mail");
        txtEmail.setWidth("100%");
        details.addComponent(txtEmail);

        txtPhone = new TextField("Phone");
        txtPhone.setWidth("100%");
        details.addComponent(txtPhone);

        cbxLocation = new ComboBox("Location");
        cbxLocation.setWidth("50%");
        cbxLocation.setNullSelectionAllowed(false);
        cbxLocation.setNewItemsAllowed(false);
        cbxLocation.addItems(DBDefaultValues.iller);
        details.addComponent(cbxLocation);

        txtDescription = new TextArea("Description");
        txtDescription.setWidth("100%");
        txtDescription.setHeight("55px");
        details.addComponent(txtDescription);

        dfCreationDate = new DateField("Creation Date");
        dfCreationDate.setWidth("50%");
//        dfCreationDate.setRangeStart(new Date());
        dfCreationDate.setEnabled(false);
        dfCreationDate.setDateFormat("yyyy-MM-dd");
        details.addComponent(dfCreationDate);

        dfExpireDate = new DateField("Expire Date");
        dfExpireDate.setWidth("50%");
        dfExpireDate.setRangeStart(new Date());
        dfExpireDate.setDateFormat("yyyy-MM-dd");
        details.addComponent(dfExpireDate);

        cbxActive = new ComboBox("Actice/Passive");
        cbxActive.setWidth("50%");
        cbxActive.setNullSelectionAllowed(false);
        cbxActive.setNewItemsAllowed(false);
        cbxActive.addItems(true);
        cbxActive.addItems(false);
        details.addComponent(cbxActive);

        /** set edilecek değerler */
        txtCompanyId.setValue(String.valueOf(company.getCompanyID()));
        txtCompanyName.setValue(company.getCompanyName());
        txtEmail.setValue(company.getEmail());
        txtPhone.setValue(company.getPhone());
        cbxLocation.setValue(company.getLocation());
        txtDescription.setValue(company.getDescription());
        dfExpireDate.setValue(company.getExpireDate());
        dfCreationDate.setValue(company.getCreationDate());
        cbxActive.setValue(company.getInActive());

        return companyContent;
    }

    private Component buildFooter() {

        HorizontalLayout footer = new HorizontalLayout();
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

                company.setCompanyName(txtCompanyName.getValue());
                company.setEmail(txtEmail.getValue());
                company.setPhone(txtPhone.getValue());
                company.setLocation((String) cbxLocation.getValue());
                company.setDescription(txtDescription.getValue());
                company.setExpireDate(dfExpireDate.getValue());
                company.setInActive((Boolean) cbxActive.getValue());

                UtilsForSpring.getSingleBeanOfType(ICompanyService.class).save(company);

                Notification success = new Notification("Company updated successfully");
                success.setDelayMsec(2000);
                success.setStyleName("bar success small");
                success.setPosition(Position.BOTTOM_CENTER);
                success.show(Page.getCurrent());

                companyTable.refreshRowCache();
                close();
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
            Notification.show("Expire date must be min. 3 months!", Type.WARNING_MESSAGE);
            return false;

        } else if (txtCompanyName.getValue().length() < 5) {
            Notification.show("Company name must be more than 5 letters!", Type.WARNING_MESSAGE);
            return false;

        } else if (!txtEmail.getValue().contains(".") || !txtEmail.getValue().contains("@")) {
            Notification.show("e-mail is invalid!", Type.WARNING_MESSAGE);
            return false;

        } else if (txtPhone.getValue().length() < 5 || !txtPhone.getValue().matches("[0-9]+")) {
            Notification.show("Invalid phone number!", Type.HUMANIZED_MESSAGE);
            return false;

        } else if ("".equals(cbxLocation.getValue()) || cbxLocation.getValue() == null) {
            Notification.show("Select a location for Company!", Type.WARNING_MESSAGE);
            return false;

        } else if (txtDescription.getValue().length() < 5) {
            Notification.show("Please give a description for Company!", Type.WARNING_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

}
