package com.ekocbiyik.component;

import com.ekocbiyik.UtilsForSpring;
import com.ekocbiyik.model.Company;
import com.ekocbiyik.model.User;
import com.ekocbiyik.service.ICompanyService;
import com.ekocbiyik.service.IUserService;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;

/**
 * Created by enbiya on 31.12.2016.
 */
public class AdminEditWindow extends Window {

    private final User admin;
    private Table adminsTable;
    private TextField txtUsername;
    private PasswordField txtPassword;
    private TextField txtEmail;
    private ComboBox cbxCompany;
    private TextField txtFirstName;
    private TextField txtLastName;
    private TextField txtPhone;
    private ComboBox cbxActive;
    private CheckBox cbxPassChange;

    private ICompanyService companyService;
    private IUserService userService;

    public AdminEditWindow(final User admin, Table adminsTable) {

        this.admin = admin;
        this.adminsTable = adminsTable;

        userService = UtilsForSpring.getSingleBeanOfType(IUserService.class);
        companyService = UtilsForSpring.getSingleBeanOfType(ICompanyService.class);

        addStyleName("profile-window");
        Responsive.makeResponsive(this);

        setModal(true);
        setResizable(false);
        setClosable(true);
        setHeight(100.0f, Unit.PERCENTAGE);
        addCloseShortcut(KeyCode.ESCAPE, null);
        setCaption("Edit Admin");

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

        HorizontalLayout adminContent = new HorizontalLayout();
        adminContent.setWidth("100%");
        adminContent.setSpacing(true);
        adminContent.setMargin(new MarginInfo(false, true, false, true));
        adminContent.addStyleName("profile-form");

        VerticalLayout pict = new VerticalLayout();
        pict.setSizeUndefined();
        pict.setSpacing(true);
        Image profilePict = new Image(null, new ThemeResource("img/profile-pic-300px.jpg"));
        profilePict.setWidth(100.0f, Unit.PIXELS);
        pict.addComponent(profilePict);

        adminContent.addComponent(pict);

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        adminContent.addComponent(details);
        adminContent.setExpandRatio(details, 1);

        /** Credentials bilgileri */
        Label credentialsTab = new Label("Admin Info");
        credentialsTab.addStyleName(ValoTheme.LABEL_H4);
        credentialsTab.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(credentialsTab);

        /** username değiştirilemez */
        txtUsername = new TextField("Username");
        txtUsername.setWidth("100%");
        txtUsername.setEnabled(false);
        details.addComponent(txtUsername);

        HorizontalLayout wrap = new HorizontalLayout();
        wrap.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        wrap.setSpacing(true);
        wrap.setCaption("Password");
        details.addComponent(wrap);

        cbxPassChange = new CheckBox("", false);
        cbxPassChange.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent valueChangeEvent) {
                txtPassword.setEnabled(cbxPassChange.getValue());
                txtPassword.setValue(cbxPassChange.getValue() ? "" : admin.getPassword());
            }
        });
        wrap.addComponent(cbxPassChange);

        txtPassword = new PasswordField();
        txtPassword.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        txtPassword.setWidth("100%");
        txtPassword.setEnabled(cbxPassChange.getValue());
        wrap.addComponent(txtPassword);


        txtEmail = new TextField("E-mail");
        txtEmail.setWidth("100%");
        details.addComponent(txtEmail);


        BeanItemContainer<Company> itemContainer = new BeanItemContainer<Company>(Company.class);
        itemContainer.addAll(companyService.getAllCompanies());

        cbxCompany = new ComboBox("Company", itemContainer);
        cbxCompany.setWidth("50%");
        cbxCompany.setNullSelectionAllowed(false);
        cbxCompany.setNewItemsAllowed(false);
        cbxCompany.setImmediate(true);
        cbxCompany.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        cbxCompany.setItemCaptionPropertyId("companyName");
        details.addComponent(cbxCompany);

        txtFirstName = new TextField("First Name");
        txtFirstName.setWidth("100%");
        details.addComponent(txtFirstName);

        txtLastName = new TextField("Last Name");
        txtLastName.setWidth("100%");
        details.addComponent(txtLastName);

        txtPhone = new TextField("Phone");
        txtPhone.setWidth("100%");
        details.addComponent(txtPhone);

        cbxActive = new ComboBox("Actice/Passive");
        cbxActive.setWidth("50%");
        cbxActive.setNullSelectionAllowed(false);
        cbxActive.setNewItemsAllowed(false);
        cbxActive.addItems(true);
        cbxActive.addItems(false);
        details.addComponent(cbxActive);

        txtUsername.setValue(admin.getUsername());
        txtPassword.setValue(admin.getPassword());
        txtEmail.setValue(admin.getEmail());
        cbxCompany.select(cbxCompany.getItemIds().toArray()[getCompanyIndex()]);
        txtFirstName.setValue(admin.getFirstName());
        txtLastName.setValue(admin.getLastName());
        txtPhone.setValue(admin.getPhone());
        cbxActive.setValue(admin.isInActive());

        return adminContent;
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

                if (cbxPassChange.getValue()) {
                    admin.setPassword(txtPassword.getValue());
                }

                admin.setEmail(txtEmail.getValue());
                admin.setCompany((Company) cbxCompany.getValue());
                admin.setFirstName(txtFirstName.getValue());
                admin.setLastName(txtLastName.getValue());
                admin.setPhone(txtPhone.getValue());
                admin.setInActive((Boolean) cbxActive.getValue());

                MessageBox.createQuestion()
                        .withCaption("Uyarı!")
                        .withMessage("Güncelleme yapmak istediğinizden emin misiniz?")
                        .withYesButton(() -> {
                            userService.save(admin);
                        }, ButtonOption.caption("Evet"))
                        .withNoButton(() -> {
                            close();
                        }, ButtonOption.caption("Hayır"))
                        .open();



                Notification success = new Notification("Admin has been updated successfully");
                success.setDelayMsec(2000);
                success.setStyleName("bar success small");
                success.setPosition(Position.BOTTOM_CENTER);
                success.show(Page.getCurrent());

                adminsTable.refreshRowCache();
                close();
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
            if (((Company) cbxCompany.getItemIds().toArray()[i]).getCompanyID() == admin.getCompany().getCompanyID()) {
                return i;
            }
        }

        /** buraya hiç düşmeyeceği için problem yok*/
        return 0;
    }

    private boolean isFormValuesOK() {

        if (cbxPassChange.getValue() && txtPassword.getValue().length() < 5) {
            Notification.show("Password must be more than 5 letters!", Type.WARNING_MESSAGE);
            return false;
        } else if (!txtEmail.getValue().contains("@") || !txtEmail.getValue().contains(".")) {
            Notification.show("e-mail is invalid!", Type.WARNING_MESSAGE);
            return false;
        } else if (cbxCompany.getValue() == null) {
            Notification.show("Select a Company for Admin!", Type.WARNING_MESSAGE);
            return false;
        } else if (txtFirstName.getValue().length() < 3) {
            Notification.show("Firstname must be more than 3 letters!", Type.WARNING_MESSAGE);
            return false;
        } else if (txtLastName.getValue().length() < 2) {
            Notification.show("Lastname must be more than 2 letters!", Type.WARNING_MESSAGE);
            return false;
        } else if (txtPhone.getValue().length() < 5 || !txtPhone.getValue().matches("[0-9]+")) {
            Notification.show("Invalid phone number!", Type.HUMANIZED_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

}
