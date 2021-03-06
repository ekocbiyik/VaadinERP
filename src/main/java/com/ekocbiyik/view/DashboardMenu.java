package com.ekocbiyik.view;

import com.google.common.eventbus.Subscribe;
import com.ekocbiyik.event.DashboardEvent.NotificationsCountUpdatedEvent;
import com.ekocbiyik.event.DashboardEvent.PostViewChangeEvent;
import com.ekocbiyik.event.DashboardEvent.ProfileUpdatedEvent;
import com.ekocbiyik.event.DashboardEvent.UserLoggedOutEvent;
import com.ekocbiyik.event.DashboardEventBus;
import com.ekocbiyik.model.User;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by enbiya on 15.12.2016.
 */
public final class DashboardMenu extends CustomComponent {


    public static final String ID = "dashboard-menu";
    public static final String NOTIFICATIONS_BADGE_ID = "dashboard-menu-notifications-badge";
    private static final String STYLE_VISIBLE = "valo-menu-visible";
    private MenuItem settingsItem;
    private Label userRoleBadge;


    public DashboardMenu() {

        setPrimaryStyleName("valo-menu");
        setId(ID);
        setSizeUndefined();

        DashboardEventBus.register(this);

        setCompositionRoot(buildContent());
    }

    private Component buildContent() {

        final CssLayout menuContent = new CssLayout();
        menuContent.addStyleName("sidebar");
        menuContent.addStyleName(ValoTheme.MENU_PART);
        menuContent.addStyleName("no-vertical-drag-hints");
        menuContent.addStyleName("no-horizontal-drag-hints");
        menuContent.setWidth(null);
        menuContent.setHeight("100%");

        menuContent.addComponent(buildTitle());
        menuContent.addComponent(buildUserMenu());
        menuContent.addComponent(buildToggleButton());
        menuContent.addComponent(buildMenuItems());

        return menuContent;
    }

    private Component buildTitle() {

        Label logo = new Label("<strong>Vaadin</strong> ERP", ContentMode.HTML);
        logo.setSizeUndefined();

        HorizontalLayout logoWrapper = new HorizontalLayout(logo);
        logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        logoWrapper.setStyleName("valo-menu-title");

        return logoWrapper;
    }

    private User getCurrentUser() {

        User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());

        return user;
    }

    private Component buildUserMenu() {
        final MenuBar settings = new MenuBar();
        settings.addStyleName("user-menu");

        final User user = getCurrentUser();
        settingsItem = settings.addItem("", new ThemeResource("img/profile-pic-300px.jpg"), null);
        updateUserName(null);
        settingsItem.addItem("Edit Profile", new Command() {
            @Override
            public void menuSelected(MenuItem menuItem) {
                Notification.show("ProfilePreferencesWindow.open(user, false); coming soon..");
            }
        });
        settingsItem.addItem("Preferences", new Command() {
            @Override
            public void menuSelected(MenuItem menuItem) {
                Notification.show("ProfilePreferencesWindow.open(user, true); coming soon..");
            }
        });
        settingsItem.addSeparator();
        settingsItem.addItem("Sign Out", new Command() {
            @Override
            public void menuSelected(MenuItem menuItem) {
                DashboardEventBus.post(new UserLoggedOutEvent());
            }
        });

        return settings;
    }

    private Component buildToggleButton() {

        Button valoMenuToggleButton = new Button("Menu", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                if (getCompositionRoot().getStyleName().contains(STYLE_VISIBLE)) {
                    getCompositionRoot().removeStyleName(STYLE_VISIBLE);
                } else {
                    getCompositionRoot().addStyleName(STYLE_VISIBLE);
                }
            }
        });

        valoMenuToggleButton.setIcon(FontAwesome.LIST);
        valoMenuToggleButton.addStyleName("valo-menu-toggle");
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_SMALL);

        return valoMenuToggleButton;
    }

    private Component buildMenuItems() {

        CssLayout menuItemsLayout = new CssLayout();
        menuItemsLayout.addStyleName("valo-menuitems");

        /** burada o anki user'ın rolünü al, o role ait menüleri getir */
        User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
        List<DashboardViewType> userRoleViews = DashboardViewType.getViewsByRole(user.getUserRole());

        for (DashboardViewType view : userRoleViews) {

            // menu createle alakalıysa
            if (view == DashboardViewType.SYS_NEWCOMPANY || view == DashboardViewType.ADM_NEWACCOUNT) {
                menuItemsLayout.addComponent(buildMenuGroupLabel("Create Menu"));
            }

            // menu listelemeyle alakalıysa
            if (view == DashboardViewType.SYS_SHOWCOMPANIES
                    || view == DashboardViewType.ADM_SHOWACCOUNT) {
                menuItemsLayout.addComponent(buildMenuGroupLabel("Show Menu"));
            }

            Component menuItemComponent = new ValoMenuItemButton(view);
            if (view == DashboardViewType.DASHBOARD) {
                userRoleBadge = new Label("", ContentMode.HTML);
                userRoleBadge.setId(NOTIFICATIONS_BADGE_ID);
                menuItemComponent = buildBadgeWrapper(menuItemComponent, userRoleBadge);
            }
            menuItemsLayout.addComponent(menuItemComponent);
        }

        return menuItemsLayout;
    }

    public void updateUserName(final ProfileUpdatedEvent event) {
        User user = getCurrentUser();
        settingsItem.setText(user.getFirstName() + " " + user.getLastName());
    }

    private Component buildBadgeWrapper(final Component menuItemButton, final Label badgeLabel) {

        CssLayout dashboardWrapper = new CssLayout(menuItemButton);
        dashboardWrapper.addStyleName("badgewrapper");
        dashboardWrapper.addStyleName(ValoTheme.MENU_ITEM);
        badgeLabel.addStyleName(ValoTheme.MENU_BADGE);
        badgeLabel.setWidthUndefined();
        badgeLabel.setVisible(true);
        badgeLabel.setValue(getCurrentUser().getUserRole().name().substring(0, 3));
        dashboardWrapper.addComponent(badgeLabel);

        return dashboardWrapper;
    }

    private Label buildMenuGroupLabel(String menuName) {

        Label label = new Label(menuName, ContentMode.HTML);
        label.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
        label.addStyleName(ValoTheme.LABEL_H4);
        label.setSizeUndefined();

        return label;
    }

    @Override
    public void attach() {
        super.attach();
//        updateNotificationsCount(null);
    }

    public void updateNotificationsCount(final NotificationsCountUpdatedEvent event) {
//        int unreadNotificationsCount = 2;
//        userRoleBadge.setValue(String.valueOf(unreadNotificationsCount));
//        userRoleBadge.setVisible(unreadNotificationsCount > 0);
    }

    @Subscribe
    public void postViewChange(final PostViewChangeEvent event) {
        getCompositionRoot().removeStyleName(STYLE_VISIBLE);
    }

    public final class ValoMenuItemButton extends Button {

        private static final String STYLE_SELECTED = "selected";

        private final DashboardViewType view;

        public ValoMenuItemButton(final DashboardViewType view) {

            this.view = view;
            setPrimaryStyleName("valo-menu-item");
            setIcon(view.getIcon());
//            setCaption(view.getViewName().substring(0, 1).toUpperCase() + view.getViewName().substring(1));
            setCaption(view.getCaption());
            DashboardEventBus.register(this);
            addClickListener((ClickListener) clickEvent -> {
                        UI.getCurrent().getNavigator().navigateTo(view.getViewName());
                    }
            );
        }

        @Subscribe
        public void postViewChange(final PostViewChangeEvent event) {
            removeStyleName(STYLE_SELECTED);
            if (event.getView() == view) {
                addStyleName(STYLE_SELECTED);
            }
        }
    }

}
