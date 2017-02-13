package com.ekocbiyik.view.sysadminrole;

import com.ekocbiyik.UtilsForSpring;
import com.ekocbiyik.component.AdminEditWindow;
import com.ekocbiyik.event.DashboardEventBus;
import com.ekocbiyik.model.User;
import com.ekocbiyik.service.IUserService;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by enbiya on 18.12.2016.
 */
public class SysShowAdminsView extends Panel implements View {

    private VerticalLayout root;
    private Table adminsTable;

    public SysShowAdminsView() {

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

    }

    private Component buildContent() {

        List<User> adminList = UtilsForSpring.getSingleBeanOfType(IUserService.class).getAllAdmins();
        BeanItemContainer<User> itemContainer = new BeanItemContainer<User>(User.class);

        itemContainer.addNestedContainerProperty("company.companyName");
        itemContainer.addAll(adminList);

        adminsTable = new Table("All admins", itemContainer);
        adminsTable.setHeight("100%");
        adminsTable.addStyleName(ValoTheme.TABLE_COMPACT);
        adminsTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
        adminsTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        adminsTable.setSelectable(true);

        adminsTable.addGeneratedColumn("edit", new ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Button b = new Button("Edit Admin");
                b.addStyleName(ValoTheme.BUTTON_DANGER);
                b.addClickListener(new ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent clickEvent) {

                        Window w = new AdminEditWindow((User) itemId, adminsTable);
                        UI.getCurrent().addWindow(w);
                        w.focus();
                    }
                });
                return b;
            }
        });


        return adminsTable;
    }

    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {

    }
}
