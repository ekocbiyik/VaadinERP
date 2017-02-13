package com.ekocbiyik.view.sysadminrole;

import com.ekocbiyik.UtilsForSpring;
import com.ekocbiyik.component.CompanyEditWindow;
import com.ekocbiyik.event.DashboardEventBus;
import com.ekocbiyik.model.Company;
import com.ekocbiyik.service.ICompanyService;
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
public class SysShowCompaniesView extends Panel implements View {

    private VerticalLayout root;
    private Table companiesTable;

    public SysShowCompaniesView() {

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


        List<Company> companyList = UtilsForSpring.getSingleBeanOfType(ICompanyService.class).getAllCompanies();
        BeanItemContainer<Company> itemContainer = new BeanItemContainer<Company>(Company.class);
        itemContainer.addAll(companyList);

        companiesTable = new Table("All Companies", itemContainer);
        companiesTable.setHeight("100%");
        companiesTable.addStyleName(ValoTheme.TABLE_COMPACT);
        companiesTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
        companiesTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        companiesTable.setSelectable(true);

        companiesTable.addGeneratedColumn("edit", new ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Button b = new Button("Edit Company");
                b.addStyleName(ValoTheme.BUTTON_DANGER);
                b.addClickListener(new ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent clickEvent) {

                        Window w = new CompanyEditWindow((Company) itemId, companiesTable);
                        UI.getCurrent().addWindow(w);
                        w.focus();
                    }
                });
                return b;
            }
        });

        return companiesTable;
    }

    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {

    }
}
