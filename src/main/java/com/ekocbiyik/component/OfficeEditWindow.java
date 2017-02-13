package com.ekocbiyik.component;

import com.ekocbiyik.DBDefaultValues;
import com.ekocbiyik.UtilsForSpring;
import com.ekocbiyik.model.Company;
import com.ekocbiyik.model.Office;
import com.ekocbiyik.model.User;
import com.ekocbiyik.service.ICompanyService;
import com.ekocbiyik.service.IOfficeService;
import com.ekocbiyik.service.IUserService;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
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
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by enbiya on 31.12.2016.
 */
public class OfficeEditWindow extends Window {

    private User currentUser;
    private IUserService userService;
    private ICompanyService companyService;
    private Office office;
    private Table officesTable;
    private TextField txtOfficeId;
    private ComboBox cbxCompanyName;
    private TextField txtOfficeName;
    private TextField txtEmail;
    private TextField txtPhone;
    private ComboBox cbxOwner;
    private ComboBox cbxLocation;
    private ComboBox cbxActive;

    public OfficeEditWindow(final Office office, Table officesTable) {

        this.office = office;
        this.officesTable = officesTable;

        currentUser = VaadinSession.getCurrent().getAttribute(User.class);
        companyService = UtilsForSpring.getSingleBeanOfType(ICompanyService.class);
        userService = UtilsForSpring.getSingleBeanOfType(IUserService.class);

        addStyleName("profile-window");
        Responsive.makeResponsive(this);

        setModal(true);
        setResizable(false);
        setClosable(true);
        setHeight(100.0f, Unit.PERCENTAGE);
        addCloseShortcut(KeyCode.ESCAPE, null);
        setCaption("Edit Office");

        VerticalLayout content = new VerticalLayout();
        content.setHeight("100%");
        content.setMargin(true);
        setContent(content);

        Component c = buildContent();
        content.addComponent(c);
        content.setExpandRatio(c, 1f);

        content.addComponent(buildFooter());

    }

    private Component buildContent() {

        HorizontalLayout officeContent = new HorizontalLayout();
        officeContent.setWidth("100%");
        officeContent.setMargin(new MarginInfo(false, true, false, true));
        officeContent.addStyleName("profile-form");

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        officeContent.addComponent(details);
        officeContent.setExpandRatio(details, 1);

        Label credentialsTab = new Label("Office Info");
        credentialsTab.addStyleName(ValoTheme.LABEL_H4);
        credentialsTab.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(credentialsTab);

        /** officeId değiştirilemez */
        txtOfficeId = new TextField("Office Id");
        txtOfficeId.setWidth("100%");
        txtOfficeId.setEnabled(false);
        details.addComponent(txtOfficeId);

        BeanItemContainer<Company> itemContainer = new BeanItemContainer<Company>(Company.class);
        itemContainer.addAll(companyService.getAllCompanies());

        cbxCompanyName = new ComboBox("Company Name", itemContainer);
        cbxCompanyName.setWidth("50%");
        cbxCompanyName.setEnabled(false);
        cbxCompanyName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        cbxCompanyName.setItemCaptionPropertyId("companyName");
        details.addComponent(cbxCompanyName);

        txtOfficeName = new TextField("Office Name");
        txtOfficeName.setWidth("100%");
        details.addComponent(txtOfficeName);

        txtEmail = new TextField("E-mail");
        txtEmail.setWidth("100%");
        details.addComponent(txtEmail);

        txtPhone = new TextField("Phone");
        txtPhone.setWidth("100%");
        details.addComponent(txtPhone);

        BeanItemContainer<User> userContainer = new BeanItemContainer<User>(User.class);
        userContainer.addAll(userService.getAccountantsByCompany(currentUser.getCompany()));

        cbxOwner = new ComboBox("Owner", userContainer);
        cbxOwner.setWidth("50%");
        cbxOwner.setInputPrompt("Please select accountant");
        cbxOwner.setNewItemsAllowed(false);
        cbxOwner.setNullSelectionAllowed(false);
        cbxOwner.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        cbxOwner.setItemCaptionPropertyId("firstName");
        details.addComponent(cbxOwner);

        cbxLocation = new ComboBox("Location");
        cbxLocation.setWidth("50%");
        cbxLocation.setInputPrompt("Please select Location");
        cbxLocation.setNewItemsAllowed(false);
        cbxLocation.setNullSelectionAllowed(false);
        cbxLocation.addItems(DBDefaultValues.iller);
        details.addComponent(cbxLocation);

        cbxActive = new ComboBox("Actice/Passive");
        cbxActive.setWidth("50%");
        cbxActive.setNullSelectionAllowed(false);
        cbxActive.setNewItemsAllowed(false);
        cbxActive.addItems(true);
        cbxActive.addItems(false);
        details.addComponent(cbxActive);


        txtOfficeId.setValue(office.getOfficeId());
        txtOfficeName.setValue(office.getOfficeName());
        cbxCompanyName.select(cbxCompanyName.getItemIds().toArray()[getCompanyIndex()]);
        txtEmail.setValue(office.getEmail());
        txtPhone.setValue(office.getPhone());
        cbxOwner.select(cbxOwner.getItemIds().toArray()[getOwnerIndex()]);
        cbxLocation.setValue(office.getLocation());
        cbxActive.setValue(office.isInActive());

        return officeContent;
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

                try {

                    if (!isFormValuesOK()) {
                        return;
                    }

                    office.setOfficeName(txtOfficeName.getValue());
                    office.setEmail(txtEmail.getValue());
                    office.setPhone(txtPhone.getValue());
                    office.setOwner((User) cbxOwner.getValue());
                    office.setLocation((String) cbxLocation.getValue());
                    office.setInActive((Boolean) cbxActive.getValue());

                    UtilsForSpring.getSingleBeanOfType(IOfficeService.class).save(office);

                    Notification success = new Notification("Accountant updated successfully");
                    success.setDelayMsec(2000);
                    success.setStyleName("bar success small");
                    success.setPosition(Position.BOTTOM_CENTER);
                    success.show(Page.getCurrent());

                    officesTable.refreshRowCache();
                    officesTable.select(office);
                    close();

                } catch (Exception e) {
                    Notification.show("Oops! An error occured.");
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
            if (((Company) cbxCompanyName.getItemIds().toArray()[i]).getCompanyID() == currentUser.getCompany().getCompanyID()) {
                return i;
            }
        }

        /** buraya hiç düşmeyeceği için problem yok*/
        return 0;
    }

    private int getOwnerIndex() {

        /** bu metot company combobox set vaue yapamadığımız için geçici çözüm!!*/
        for (int i = 0; i < cbxOwner.getItemIds().toArray().length; i++) {
            if (((User) cbxOwner.getItemIds().toArray()[i]).getUserId() == currentUser.getUserId()) {
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
            Notification.show("email is invalid!", Type.WARNING_MESSAGE);
            return false;
        } else if (txtPhone.getValue().length() < 5 || !txtPhone.getValue().matches("[0-9]+")) {
            Notification.show("Invalid phone number!", Type.HUMANIZED_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

}
