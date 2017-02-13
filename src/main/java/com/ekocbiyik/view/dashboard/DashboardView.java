package com.ekocbiyik.view.dashboard;

import com.ekocbiyik.enums.EUserRole;
import com.ekocbiyik.event.DashboardEventBus;
import com.ekocbiyik.model.User;
import com.ekocbiyik.view.account.AccountDashboardView;
import com.ekocbiyik.view.adminrole.AdminDashboardView;
import com.ekocbiyik.view.sysadminrole.SysAdminDashboardView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by enbiya on 15.12.2016.
 */
public class DashboardView extends Panel implements View {

    public static final String TITLE_ID = "dashboard-title";
    public static final String EDIT_ID = "dashboard-edit";
    private final VerticalLayout root;
    private Label titleLabel;
    private VerticalLayout dashboardPanels;

    public DashboardView() {

        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);


        root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.addStyleName("dashboard-view");/** diğer tüm panellerde de bu kısım aynen yazılıyor, sebebi her panel için ayrıca css yazmayalım */
        setContent(root);
        Responsive.makeResponsive(root);

        root.addComponent(buildHeader());

        Component content = buildContent();
        root.addComponent(content);
        root.setExpandRatio(content, 1);

//        root.addLayoutClickListener(new LayoutClickListener() {
//            @Override
//            public void layoutClick(LayoutClickEvent layoutClickEvent) {
//                DashboardEventBus.post(new CloseOpenWindowsEvent());
//            }
//        });
    }

    private Component buildHeader() {

        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);

        titleLabel = new Label("Dashboard");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        Component edit = buildEditButton();
        HorizontalLayout tools = new HorizontalLayout(edit);
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }

    private Component buildEditButton() {
        Button result = new Button();
        result.setId(EDIT_ID);
        result.setIcon(FontAwesome.EDIT);
        result.addStyleName("icon-edit");
        result.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        result.setDescription("Edit Dashboard");
        result.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                Notification.show("getUI().addWindow(new Label(\"Edit\"));");
            }
        });
        return result;
    }

    private Component buildContent() {


        /** Dashboard menüsü tüm yetki tipleri için ortak olacak,
         *  fakat yetki tipine göre içerik farklı olacak. Bu yüzden yetki tipine göre hangi
         *  içeriğin geleceğini burada beliremek gerekir.
         *
         *  sysadmin ise -> new sysadmin()
         *  admin ise -> new admin()
         *  user ise -> new user()
         * */


        dashboardPanels = new VerticalLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(dashboardPanels);

        User currentUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());

        if (currentUser.getUserRole() == EUserRole.SYS_ADMIN) {
            dashboardPanels.addComponent(new SysAdminDashboardView().getSysAdminDashboardView());
        }

        if (currentUser.getUserRole() == EUserRole.ADMIN) {
            dashboardPanels.addComponent(new AdminDashboardView().getAdminDashboardView());
        }
        if (currentUser.getUserRole() == EUserRole.ACCOUNT) {
            dashboardPanels.addComponent(new AccountDashboardView().getAccountDashboardView());
        }

        return dashboardPanels;
    }

    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {
    }
}
