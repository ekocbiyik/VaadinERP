package com.ekocbiyik.view.account;

import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by enbiya on 18.12.2016.
 */
public class AccountDashboardView {

    private VerticalLayout root;

    public Component getAccountDashboardView() {

        if (root == null) {

            root = new VerticalLayout();
            root.setSizeFull();
            root.setMargin(true);
            root.addStyleName("dashboard-view");
            Responsive.makeResponsive(root);
            root.addComponent(getAccountChartsPanel());
        }

        return root;
    }

    private HorizontalLayout getAccountChartsPanel() {

        HorizontalLayout chartsPanel = new HorizontalLayout();
        chartsPanel.setSizeFull();
        chartsPanel.setMargin(true);
        Responsive.makeResponsive(chartsPanel);

        chartsPanel.addComponent(getTotalOfficeChart());
        chartsPanel.addComponent(getTotalTaskChart());

        return chartsPanel;
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
