package com.ekocbiyik.component;

import com.ekocbiyik.UtilsForSpring;
import com.ekocbiyik.model.ResourceRecords;
import com.ekocbiyik.model.User;
import com.ekocbiyik.service.IResourceRecordService;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enbiya on 31.12.2016.
 */
public class SerialSearchWindow extends Window {

    private TextField txtSerialNumber;
    private Table resultTable;
    private IResourceRecordService recordService;

    public SerialSearchWindow() {

        addStyleName("profile-window");
        Responsive.makeResponsive(this);

        setModal(true);
        setResizable(true);
        setClosable(true);
        addCloseShortcut(KeyCode.ESCAPE, null);
        setCaption("Search Serial Number");
        setIcon(FontAwesome.SEARCH);

        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        setContent(content);

        Component c = buildContent();
        content.addComponent(c);
        content.setExpandRatio(c, 1f);

    }

    private Component buildContent() {

        VerticalLayout popupContent = new VerticalLayout();
        popupContent.setSpacing(true);
        popupContent.addStyleName("profile-form");


        HorizontalLayout wrap = new HorizontalLayout();
        wrap.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        wrap.setSpacing(true);
        popupContent.addComponent(wrap);

        txtSerialNumber = new TextField();
        txtSerialNumber.setInputPrompt("Serial Number..");
        txtSerialNumber.setWidth("50%");
        wrap.addComponent(txtSerialNumber);

        Button showResults = new Button("Show Results", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {

                if (txtSerialNumber.getValue().equals("") || txtSerialNumber.getValue() == null) {
                    Notification.show("Please input a serial number!", Type.WARNING_MESSAGE);
                    return;
                }

                User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
                List<ResourceRecords> recordsList = recordService.getResourceRecordsBySerialNumber(txtSerialNumber.getValue(), currentUser);
                resultTable.getContainerDataSource().removeAllItems();

                for (ResourceRecords r : recordsList) {
                    resultTable.getContainerDataSource().addItem(r);
                }
            }
        });

        wrap.addComponent(showResults);


        recordService = UtilsForSpring.getSingleBeanOfType(IResourceRecordService.class);
        BeanItemContainer<ResourceRecords> itemContainer = new BeanItemContainer<ResourceRecords>(ResourceRecords.class);
        itemContainer.addNestedContainerProperty("office.officeName");
        itemContainer.addAll(new ArrayList<>());

        resultTable = new Table("", itemContainer);
        resultTable.setHeight("350px");
        resultTable.setWidth("100%");
        resultTable.addStyleName(ValoTheme.TABLE_COMPACT);
        resultTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        resultTable.setSelectable(true);
        resultTable.setPageLength(10);
        popupContent.addComponent(resultTable);

        return popupContent;
    }

}
