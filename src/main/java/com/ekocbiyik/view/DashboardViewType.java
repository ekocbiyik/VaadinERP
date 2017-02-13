package com.ekocbiyik.view;

import com.ekocbiyik.enums.EUserRole;
import com.ekocbiyik.view.account.OfficeReportsView;
import com.ekocbiyik.view.adminrole.AdminNewAccountView;
import com.ekocbiyik.view.adminrole.AdminNewOfficeView;
import com.ekocbiyik.view.adminrole.AdminShowAccountsView;
import com.ekocbiyik.view.adminrole.AdminShowOfficesView;
import com.ekocbiyik.view.dashboard.DashboardView;
import com.ekocbiyik.view.sysadminrole.SysNewAdminView;
import com.ekocbiyik.view.sysadminrole.SysNewCompanyView;
import com.ekocbiyik.view.sysadminrole.SysShowAccountsView;
import com.ekocbiyik.view.sysadminrole.SysShowAdminsView;
import com.ekocbiyik.view.sysadminrole.SysShowCompaniesView;
import com.ekocbiyik.view.sysadminrole.SysShowOfficesView;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enbiya on 15.12.2016.
 */
public enum DashboardViewType {

    /**
     * tüm menüler burada olsun login olduktan sonra yetkiye göre menüleri manuel olarak oluştur
     */

    //Ortak Ana ekran
    DASHBOARD("dashboard", DashboardView.class, FontAwesome.HOME, false, "Dashboard"),

    //SYSADMIN panelleri
    SYS_NEWCOMPANY("newcompany", SysNewCompanyView.class, FontAwesome.INSTITUTION, false, "New Company"),
    SYS_NEWADMIN("newadmin", SysNewAdminView.class, FontAwesome.USER_PLUS, false, "New Admin"),
    SYS_SHOWCOMPANIES("showcompanies", SysShowCompaniesView.class, FontAwesome.UNIVERSITY, false, "Show Companies"),
    SYS_SHOWADMINS("showadmins", SysShowAdminsView.class, FontAwesome.USER_SECRET, false, "Show Admins"),
    SYS_SHOWACCOUNTS("showaccounts", SysShowAccountsView.class, FontAwesome.USERS, false, "Show Accountants"),
    SYS_SHOWOFFICES("showoffices", SysShowOfficesView.class, FontAwesome.INDUSTRY, false, "Show Offices"),

    //ADMIN panelleri
    ADM_NEWACCOUNT("newaccount", AdminNewAccountView.class, FontAwesome.USER_PLUS, false, "New Accountant"),
    ADM_NEWOFFICE("newoffice", AdminNewOfficeView.class, FontAwesome.INDUSTRY, false, "New Office"),
    ADM_SHOWACCOUNT("showaccounts", AdminShowAccountsView.class, FontAwesome.USERS, false, "Show Accountants"),
    ADM_SHOWOFFICE("showoffices", AdminShowOfficesView.class, FontAwesome.CUBES, false, "Show Offices"),

    //ACCOUNT panelleri
    ACC_OFFICEREPORTS("officesreports", OfficeReportsView.class, FontAwesome.BAR_CHART_O, false, "Office Reports");


    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;
    private final String caption;

    DashboardViewType(final String viewName, final Class<? extends View> viewClass, final Resource icon, final boolean stateful, String caption) {
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
        this.caption = caption;
    }

    public static DashboardViewType getByViewName(final String viewName) {
        DashboardViewType result = null;
        for (DashboardViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }

    public static List<DashboardViewType> getViewsByRole(EUserRole userRole) {

        List<DashboardViewType> userRoleViews = null;

        if (userRole == EUserRole.SYS_ADMIN) {
            userRoleViews = DashboardViewType.getSysAdminViews();
        } else if (userRole == EUserRole.ADMIN) {
            userRoleViews = DashboardViewType.getAdminViews();
        } else if (userRole == EUserRole.ACCOUNT) {
            userRoleViews = DashboardViewType.getAccountViews();
        }

        return userRoleViews;
    }

    private static List<DashboardViewType> getSysAdminViews() {

        List<DashboardViewType> sysAdminViews = new ArrayList<>();
        sysAdminViews.add(DASHBOARD);
        sysAdminViews.add(SYS_NEWCOMPANY);
        sysAdminViews.add(SYS_NEWADMIN);
        sysAdminViews.add(SYS_SHOWCOMPANIES);
        sysAdminViews.add(SYS_SHOWADMINS);
        sysAdminViews.add(SYS_SHOWACCOUNTS);
        sysAdminViews.add(SYS_SHOWOFFICES);

        return sysAdminViews;
    }

    private static List<DashboardViewType> getAdminViews() {

        List<DashboardViewType> adminViews = new ArrayList<>();
        adminViews.add(DASHBOARD);
        adminViews.add(ADM_NEWACCOUNT);
        adminViews.add(ADM_NEWOFFICE);
        adminViews.add(ADM_SHOWACCOUNT);
        adminViews.add(ADM_SHOWOFFICE);

        return adminViews;
    }

    private static List<DashboardViewType> getAccountViews() {

        List<DashboardViewType> accountViews = new ArrayList<>();
        accountViews.add(DASHBOARD);
        accountViews.add(ACC_OFFICEREPORTS);

        return accountViews;
    }

    public String getViewName() {
        return viewName;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
    }

    public boolean isStateful() {
        return stateful;
    }

    public String getCaption() {
        return caption;
    }
}
