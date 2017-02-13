package com.ekocbiyik.view.adminrole;

import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by enbiya on 18.12.2016.
 */
public class AdminDashboardView {


    private VerticalLayout root;

    public Component getAdminDashboardView() {

        if (root == null) {

            root = new VerticalLayout();
            root.setSizeFull();
            root.setMargin(true);
            root.addStyleName("dashboard-view");
            Responsive.makeResponsive(root);

            root.addComponent(getAdminChartsPanel());
        }

        return root;
    }

    private HorizontalLayout getAdminChartsPanel() {

        HorizontalLayout chartsPanel = new HorizontalLayout();
        chartsPanel.setSizeFull();
        chartsPanel.setMargin(true);
        Responsive.makeResponsive(chartsPanel);

        chartsPanel.addComponent(getTotalAccountChart());
        chartsPanel.addComponent(getTotalOfficeChart());
        chartsPanel.addComponent(getTotalTaskChart());

        return chartsPanel;
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

    private Button getTotalTaskChart() {

        Button total = new Button("total tasks");
        total.setWidth("200");
        total.setHeight("100");

        return total;
    }

}
