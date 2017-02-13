package com.ekocbiyik.view.sysadminrole;

import com.ekocbiyik.UtilsForSpring;
import com.ekocbiyik.event.DashboardEventBus;
import com.ekocbiyik.model.Office;
import com.ekocbiyik.model.User;
import com.ekocbiyik.service.IOfficeService;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by enbiya on 18.12.2016.
 */
public class SysShowOfficesView extends Panel implements View {

    private VerticalLayout root;
    private Table officesTable;

    public SysShowOfficesView() {

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
        List<Office> officeList = UtilsForSpring.getSingleBeanOfType(IOfficeService.class).getAllOffices(currentUser);

        BeanItemContainer<Office> itemContainer = new BeanItemContainer<Office>(Office.class);
        itemContainer.addNestedContainerProperty("company.companyName");
        itemContainer.addNestedContainerProperty("owner.firstName");
        itemContainer.addAll(officeList);

        officesTable = new Table("All Offices", itemContainer);
        officesTable.setHeight("100%");
        officesTable.setWidth("100%");
        officesTable.addStyleName(ValoTheme.TABLE_COMPACT);
        officesTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        officesTable.setSelectable(true);


        return officesTable;
    }

    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {

    }

}
