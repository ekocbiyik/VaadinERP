package com.ekocbiyik.view.sysadminrole;

import com.ekocbiyik.UtilsForSpring;
import com.ekocbiyik.event.DashboardEventBus;
import com.ekocbiyik.model.User;
import com.ekocbiyik.service.IUserService;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by enbiya on 18.12.2016.
 */
public class SysShowAccountsView extends Panel implements View {

    private VerticalLayout root;
    private Table accountsTable;

    public SysShowAccountsView() {

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


        List<User> accountList = UtilsForSpring.getSingleBeanOfType(IUserService.class).getAllAccountants();
        BeanItemContainer<User> itemContainer = new BeanItemContainer<User>(User.class);

        itemContainer.addNestedContainerProperty("company.companyName");
        itemContainer.addAll(accountList);

        accountsTable = new Table("All Accountants", itemContainer);
        accountsTable.setHeight("100%");
        accountsTable.setWidth("100%");
        accountsTable.addStyleName(ValoTheme.TABLE_COMPACT);
//        accountsTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
        accountsTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        accountsTable.setSelectable(true);

        /** account edit yetkisi şuan için olmasın */
        accountsTable.setColumnHeader("company.companyName", "Company Name");

        return accountsTable;
    }

    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {

    }


}
