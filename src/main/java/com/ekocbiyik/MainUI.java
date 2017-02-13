package com.ekocbiyik;

import com.ekocbiyik.event.DashboardEvent.BrowserResizeEvent;
import com.ekocbiyik.event.DashboardEvent.CloseOpenWindowsEvent;
import com.ekocbiyik.event.DashboardEvent.UserLoggedOutEvent;
import com.ekocbiyik.event.DashboardEvent.UserLoginRequestedEvent;
import com.ekocbiyik.event.DashboardEventBus;
import com.ekocbiyik.model.User;
import com.ekocbiyik.service.IUserService;
import com.ekocbiyik.view.LoginView;
import com.ekocbiyik.view.MainView;
import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Locale;


/**
 * Created by enbiya on 15.12.2016.
 */
@Theme("dashboard")
@Title("VaadinERP")
public class MainUI extends UI {

    private final DashboardEventBus dashboardEventBus = new DashboardEventBus();

    public static DashboardEventBus getDashboardEventBus() {
        return ((MainUI) getCurrent()).dashboardEventBus;
    }

    public static User getCurrentUser() {
        return VaadinSession.getCurrent().getAttribute(User.class);
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        DBDefaultValues.initialize();

        setLocale(Locale.US);
        DashboardEventBus.register(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);
        setIcon(new ThemeResource("ekocbiyik.ico"));

        updateContent();

        Page.getCurrent().addBrowserWindowResizeListener(new BrowserWindowResizeListener() {
            @Override
            public void browserWindowResized(BrowserWindowResizeEvent event) {
                DashboardEventBus.post(new BrowserResizeEvent());
            }
        });

    }

    private void updateContent() {

        User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
        if (user != null) {

            setContent(new MainView());
            removeStyleName("loginview");
            getNavigator().navigateTo(getNavigator().getState());

        } else {
            setContent(new LoginView());
            addStyleName("loginview");
        }

    }

    @Subscribe
    public void closeOpenWindows(final CloseOpenWindowsEvent event) {
        for (Window window : getWindows()) {
            window.close();
        }
    }

    @Subscribe
    public void userLoginRequested(final UserLoginRequestedEvent event) {

        String username = event.getUserName();
        String password = event.getPassword();

        User user = UtilsForSpring.getSingleBeanOfType(IUserService.class).login(username, password);
        if (user == null) {
            Notification.show("Invalid username or password", Type.ERROR_MESSAGE);
            return;
        }

        VaadinSession.getCurrent().setAttribute(User.class.getName(), user);

        Notification notif = new Notification("Hos geldiniz\n " + user.getFirstName() + " " + user.getLastName(), Type.HUMANIZED_MESSAGE);
        notif.setDelayMsec(3000);
        notif.setPosition(Position.BOTTOM_RIGHT);
        notif.show(Page.getCurrent());
        updateContent();

    }

    @Subscribe
    public void userLoggedOut(final UserLoggedOutEvent event) {
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

}
