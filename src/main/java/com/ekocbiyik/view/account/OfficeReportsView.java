package com.ekocbiyik.view.account;

import com.ekocbiyik.UtilsForSpring;
import com.ekocbiyik.component.SerialSearchWindow;
import com.ekocbiyik.event.DashboardEventBus;
import com.ekocbiyik.model.Office;
import com.ekocbiyik.model.ResourceRecords;
import com.ekocbiyik.model.User;
import com.ekocbiyik.service.IOfficeService;
import com.ekocbiyik.service.IResourceRecordService;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.ekocbiyik.view.dashboard.DashboardView.EDIT_ID;

/**
 * Created by enbiya on 18.12.2016.
 */
public class OfficeReportsView extends Panel implements View {

    public static final String TITLE_ID = "dashboard-title";
    private VerticalLayout root;
    private IOfficeService officeService;
    private Label titleLabel;
    private VerticalLayout officeReportsPanels;

    private ComboBox cbxOffice;
    private PopupDateField dfStartDate;
    private PopupDateField dfEndDate;
    private Button btnShowRecords;
    private Button btnRefresh;

    private Table recordsTable;
    private Button btnExport;
    private IResourceRecordService recordService;


    public OfficeReportsView() {

        officeService = UtilsForSpring.getSingleBeanOfType(IOfficeService.class);

        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);

        root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.addStyleName("dashboard-view");
        setContent(root);
        Responsive.makeResponsive(root);

//        root.addComponent(buildHeader());

        Component content = buildContent();
        root.addComponent(content);
        root.setExpandRatio(content, 1);
    }

    private Component buildHeader() {

        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);

        titleLabel = new Label("Office Reports");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        Component search = searchButton();
        HorizontalLayout tools = new HorizontalLayout(search);
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }

    private Component searchButton() {

        Button search = new Button();
        search.setId(EDIT_ID);
        search.setIcon(FontAwesome.SEARCH);
        search.addStyleName("icon-edit");
        search.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        search.setDescription("Search Platform Serial");
        search.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {

                Window w = new SerialSearchWindow();
                UI.getCurrent().addWindow(w);
                w.focus();

            }
        });
        return search;
    }

    private Component buildContent() {

        officeReportsPanels = new VerticalLayout();
        Responsive.makeResponsive(officeReportsPanels);
        officeReportsPanels.addStyleName("dashboard-panels");
        officeReportsPanels.setSpacing(true);
        officeReportsPanels.setSizeUndefined();

        officeReportsPanels.addComponent(getRecordToolbar());
        officeReportsPanels.addComponent(getRecordsTable());

        return officeReportsPanels;
    }

    private Component getRecordToolbar() {

        HorizontalLayout details = new HorizontalLayout();
        details.setSpacing(true);
        details.setSizeUndefined();

        User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
        BeanItemContainer<Office> itemContainer = new BeanItemContainer<Office>(Office.class);
        itemContainer.addAll(officeService.getAllOffices(currentUser));

        cbxOffice = new ComboBox(null, itemContainer);
        cbxOffice.setWidth("220px");
        cbxOffice.setNullSelectionAllowed(false);
        cbxOffice.setNewItemsAllowed(false);
        cbxOffice.setInputPrompt("Select Office");
        cbxOffice.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        cbxOffice.setItemCaptionPropertyId("officeName");
        details.addComponent(cbxOffice);

        dfStartDate = new PopupDateField();
        dfStartDate.setInputPrompt("Start Date");
        dfStartDate.setWidth("140px");
        dfStartDate.setDateFormat("dd.MM.yyyy");
        dfStartDate.setValidationVisible(true);
//        dfStartDate.setRangeStart(new Date());
        details.addComponent(dfStartDate);

        dfEndDate = new PopupDateField();
        dfEndDate.setInputPrompt("End Date");
        dfEndDate.setWidth("140px");
        dfEndDate.setDateFormat("dd.MM.yyyy");
        dfEndDate.setValidationVisible(true);
//        dfEndDate.setRangeStart(new Date());
        details.addComponent(dfEndDate);

        btnShowRecords = new Button("Show Records");
        btnShowRecords.setWidth("15%");
        btnShowRecords.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                showRecords();
            }
        });
        details.addComponent(btnShowRecords);

        btnRefresh = new Button("Refresh");
        btnRefresh.setWidth("15%");
        btnRefresh.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 1);
                dfStartDate.setValue(new Date());
                dfEndDate.setValue(cal.getTime());

                if (cbxOffice.getItemIds().toArray().length > 0) {
                    cbxOffice.select(cbxOffice.getItemIds().toArray()[0]);
                }
                showRecords();
            }
        });
        details.addComponent(btnRefresh);

        if (cbxOffice.getItemIds().size() > 0) {
            cbxOffice.select(cbxOffice.getItemIds().toArray()[0]);
        }

        btnExport = new Button("Export");
        btnExport.setWidth("15%");
        btnExport.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                Notification.show("Not implemented yet!");
            }
        });
        details.addComponent(btnExport);

        Component search = searchButton();
        HorizontalLayout tools = new HorizontalLayout(search);
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        details.addComponent(tools);


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        dfStartDate.setValue(new Date());
        dfEndDate.setValue(cal.getTime());

        return details;
    }

    private Component getRecordsTable() {

        recordService = UtilsForSpring.getSingleBeanOfType(IResourceRecordService.class);
        BeanItemContainer<ResourceRecords> itemContainer = new BeanItemContainer<ResourceRecords>(ResourceRecords.class);
        itemContainer.addNestedContainerProperty("office.officeName");

        if (cbxOffice.getValue() != null) {

            List<ResourceRecords> recordsList = recordService.getAllResourceRecordsByOffice((Office) cbxOffice.getValue(), dfStartDate.getValue(), dfEndDate.getValue());
            itemContainer.addAll(recordsList);
        } else {
            itemContainer.addAll(new ArrayList<>());
        }

        /** burada kayıtlar listelenecek */
        recordsTable = new Table("Size: " + itemContainer.size(), itemContainer);
        recordsTable.setHeight("100%");
        recordsTable.setWidth("100%");
        recordsTable.addStyleName(ValoTheme.TABLE_COMPACT);
        recordsTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        recordsTable.setSelectable(true);

        return recordsTable;

    }

    private void showRecords() {

        if (cbxOffice.getValue() == null) {
            Notification.show("There is no any Office for showing records!", Type.WARNING_MESSAGE);
            return;
        }

        if (!dfStartDate.isValid() || dfStartDate.getValue() == null) {
            Notification.show("Invalid start date!", Type.WARNING_MESSAGE);
            return;
        }

        if (!dfEndDate.isValid() || dfEndDate.getValue() == null) {
            Notification.show("Invalid end date!", Type.WARNING_MESSAGE);
            return;
        }

        if (dfStartDate.getValue().after(dfEndDate.getValue())) {
            Notification.show("Please check date range between start date and end date!", Type.WARNING_MESSAGE);
            return;
        }

        List<ResourceRecords> recordsList = recordService.getAllResourceRecordsByOffice((Office) cbxOffice.getValue(), dfStartDate.getValue(), dfEndDate.getValue());
        recordsTable.getContainerDataSource().removeAllItems();

        for (ResourceRecords r : recordsList) {
            recordsTable.getContainerDataSource().addItem(r);
        }

        recordsTable.setCaption("Size: " + recordsTable.getContainerDataSource().size());
    }

    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {
    }
}
