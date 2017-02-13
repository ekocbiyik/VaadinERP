package com.ekocbiyik.view.sysadminrole;

import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by enbiya on 18.12.2016.
 */
public class SysAdminDashboardView {


    private VerticalLayout root;

    public Component getSysAdminDashboardView() {

        if (root == null) {

            root = new VerticalLayout();
            root.setSizeFull();
            root.setMargin(true);
            root.addStyleName("dashboard-view");
            Responsive.makeResponsive(root);

            root.addComponent(getSysAdminChartsPanel());
        }

        return root;
    }

    private HorizontalLayout getSysAdminChartsPanel(){

        HorizontalLayout chartsPanel = new HorizontalLayout();
        chartsPanel.setSizeFull();
        chartsPanel.setMargin(true);
        Responsive.makeResponsive(chartsPanel);

        chartsPanel.addComponent(getTotalAdminChart());
        chartsPanel.addComponent(getTotalAccountChart());
        chartsPanel.addComponent(getTotalOfficeChart());
        chartsPanel.addComponent(getTotalActiveUsersChart());

        return chartsPanel;
    }

    private Button getTotalAdminChart() {

        Button total = new Button("admins");
        total.setWidth("200");
        total.setHeight("100");

        return total;
    }

    private Button getTotalAccountChart() {

        Button total = new Button("accounts");
        total.setWidth("200");
        total.setHeight("100");

        return total;
    }

    private Button getTotalOfficeChart() {

        Button total = new Button("offices");
        total.setWidth("200");
        total.setHeight("100");

        return total;
    }

    private Button getTotalActiveUsersChart() {

        Button total = new Button("active users");
        total.setWidth("200");
        total.setHeight("100");

        return total;
    }

}
