package com.ekocbiyik.view.adminrole;

import com.ekocbiyik.DBDefaultValues;
import com.ekocbiyik.UtilsForSpring;
import com.ekocbiyik.event.DashboardEventBus;
import com.ekocbiyik.model.Company;
import com.ekocbiyik.model.Office;
import com.ekocbiyik.model.User;
import com.ekocbiyik.service.ICompanyService;
import com.ekocbiyik.service.IOfficeService;
import com.ekocbiyik.service.IUserService;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
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
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by enbiya on 18.12.2016.
 */
public class AdminNewOfficeView extends Panel implements View {

    private VerticalLayout root;
    private HorizontalLayout newOfficeContent;
    private TextField txtOfficeName;
    private TextField txtEmail;
    private TextField txtPhone;
    private ComboBox cbxCompany;
    private ComboBox cbxOwner;
    private ComboBox cbxLocation;
    private HorizontalLayout footer;

    private IUserService userService;
    private ICompanyService companyService;

    public AdminNewOfficeView() {

        companyService = UtilsForSpring.getSingleBeanOfType(ICompanyService.class);
        userService = UtilsForSpring.getSingleBeanOfType(IUserService.class);


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

        newOfficeContent = new HorizontalLayout();
        newOfficeContent.setWidth(100.0f, Unit.PERCENTAGE);
        newOfficeContent.setSpacing(true);
        newOfficeContent.setMargin(true);
        newOfficeContent.addStyleName("profile-form");


        /** ----------- */
        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        newOfficeContent.addComponent(details);
        newOfficeContent.setExpandRatio(details, 1);

        /** Credentials bilgileri */
        Label credentialsTab = new Label("Office Info");
        credentialsTab.addStyleName(ValoTheme.LABEL_H4);
        credentialsTab.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(credentialsTab);

        txtOfficeName = new TextField("Office name");
        txtOfficeName.setInputPrompt("Please give office name");
        txtOfficeName.setWidth("100%");
        details.addComponent(txtOfficeName);

        BeanItemContainer<Company> itemContainer = new BeanItemContainer<Company>(Company.class);
        itemContainer.addAll(companyService.getAllCompanies());

        cbxCompany = new ComboBox("Company Name", itemContainer);
        cbxCompany.setWidth("30%");
        cbxCompany.setEnabled(false);
        cbxCompany.setNullSelectionAllowed(false);
        cbxCompany.setNewItemsAllowed(false);
        cbxCompany.setImmediate(true);
        cbxCompany.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        cbxCompany.setItemCaptionPropertyId("companyName");
        cbxCompany.select(cbxCompany.getItemIds().toArray()[getCompanyIndex()]);
        details.addComponent(cbxCompany);

        txtEmail = new TextField("E-mail");
        txtEmail.setInputPrompt("Please give e-mail address");
        txtEmail.setWidth("100%");
        details.addComponent(txtEmail);

        txtPhone = new TextField("Phone");
        txtPhone.setInputPrompt("Please give phone number");
        txtPhone.setWidth("100%");
        details.addComponent(txtPhone);


        BeanItemContainer<User> userContainer = new BeanItemContainer<User>(User.class);
        userContainer.addAll(userService.getAccountantsByCompany(VaadinSession.getCurrent().getAttribute(User.class).getCompany()));

        cbxOwner = new ComboBox("Accountant", userContainer);
        cbxOwner.setWidth("30%");
        cbxOwner.setInputPrompt("Please select an accountant");
        cbxOwner.setNewItemsAllowed(false);
        cbxOwner.setNullSelectionAllowed(false);
        cbxOwner.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        cbxOwner.setItemCaptionPropertyId("firstName");
        details.addComponent(cbxOwner);

        cbxLocation = new ComboBox("Location");
        cbxLocation.setWidth("30%");
        cbxLocation.setInputPrompt("Please select Location");
        cbxLocation.setNewItemsAllowed(false);
        cbxLocation.setNullSelectionAllowed(false);
        cbxLocation.addItems(DBDefaultValues.iller);
        details.addComponent(cbxLocation);

        return newOfficeContent;
    }

    private Component buildFooter() {

        footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button ok = new Button("Save Office");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {

                if (!isFormValuesOK()) {
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSSS");
                String timeForId = sdf.format(new Date()) + "";
                /**burası önemli!! officeId= companyId + yyyyMMddHHmmSSS*/


                try {

                    User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
                    Office office = new Office();

                    if (cbxCompany.getValue() == null) {
                        office.setOfficeId(currentUser.getCompany().getCompanyID() + timeForId);
                        office.setCompany(currentUser.getCompany());
                    } else {
                        office.setOfficeId(((Company) cbxCompany.getValue()).getCompanyID() + timeForId);
                        office.setCompany((Company) cbxCompany.getValue());
                    }

                    office.setOfficeName(txtOfficeName.getValue());
                    office.setEmail(txtEmail.getValue());
                    office.setPhone(txtPhone.getValue());
                    office.setOwner((User) cbxOwner.getValue());
                    office.setLocation((String) cbxLocation.getValue());

                    UtilsForSpring.getSingleBeanOfType(IOfficeService.class).save(office);

                    Notification success = new Notification("Office has been saved successfully");
                    success.setDelayMsec(2000);
                    success.setStyleName("bar success small");
                    success.setPosition(Position.BOTTOM_CENTER);
                    success.show(Page.getCurrent());

                    clearFormFields();

                } catch (Exception e) {
                    Notification.show("Oops! Something went wrong.");
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
        for (int i = 0; i < cbxCompany.getItemIds().toArray().length; i++) {
            if (((Company) cbxCompany.getItemIds().toArray()[i]).getCompanyID()
                    == VaadinSession.getCurrent().getAttribute(User.class).getCompany().getCompanyID()) {
                return i;
            }
        }

        /** buraya hiç düşmeyeceği için problem yok*/
        return 0;
    }

    private boolean isFormValuesOK() {

        if (txtOfficeName.getValue().length() < 5) {
            Notification.show("Office name must be more than 5 letters!", Type.WARNING_MESSAGE);
            return false;
        } else if (!txtEmail.getValue().contains("@") || !txtEmail.getValue().contains(".")) {
            Notification.show("e-mail is invalid!", Type.WARNING_MESSAGE);
            return false;
        } else if (txtPhone.getValue().length() < 5 || !txtPhone.getValue().matches("[0-9]+")) {
            Notification.show("Invalid phone number!", Type.WARNING_MESSAGE);
            return false;
        } else if (cbxOwner.getValue() == null) {
            Notification.show("Please select an accountant!", Type.WARNING_MESSAGE);
            return false;
        } else if (cbxLocation.getValue() == null) {
            Notification.show("Please select a location for office!", Type.WARNING_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    private void clearFormFields() {
        txtEmail.clear();
        cbxLocation.clear();
        txtOfficeName.clear();
        cbxOwner.clear();
        txtPhone.clear();
    }

    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {
    }
}
