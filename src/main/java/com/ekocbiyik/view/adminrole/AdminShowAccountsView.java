package com.ekocbiyik.view.adminrole;

import com.ekocbiyik.UtilsForSpring;
import com.ekocbiyik.component.AccountEditWindow;
import com.ekocbiyik.event.DashboardEventBus;
import com.ekocbiyik.model.User;
import com.ekocbiyik.service.IUserService;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
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
public class AdminShowAccountsView extends Panel implements View {


    private VerticalLayout root;
    private Table accountsTable;

    public AdminShowAccountsView() {

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

        User currentUser = VaadinSession.getCurrent().getAttribute(User.class);

        List<User> accountList = UtilsForSpring.getSingleBeanOfType(IUserService.class).getAccountantsByCompany(currentUser.getCompany());
        BeanItemContainer<User> itemContainer = new BeanItemContainer<User>(User.class);

        itemContainer.addNestedContainerProperty("company.companyName");
        itemContainer.addAll(accountList);

        accountsTable = new Table("Accountants", itemContainer);
        accountsTable.setHeight("100%");
        accountsTable.setWidth("100%");
        accountsTable.addStyleName(ValoTheme.TABLE_COMPACT);
//        accountsTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
        accountsTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        accountsTable.setSelectable(true);

        accountsTable.addGeneratedColumn("edit", new ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Button b = new Button("Edit Accountant");
                b.addStyleName(ValoTheme.BUTTON_DANGER);
                b.addClickListener(new ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent clickEvent) {

                        Window w = new AccountEditWindow((User) itemId, accountsTable);
                        UI.getCurrent().addWindow(w);
                        w.focus();
                    }
                });
                return b;
            }
        });

        return accountsTable;
    }


    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {

    }
}
