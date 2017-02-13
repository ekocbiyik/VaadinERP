package model;

import com.ekocbiyik.enums.EUserRole;
import com.ekocbiyik.model.User;
import com.ekocbiyik.service.IUserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * Created by enbiya on 05.02.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:hibernate-config.xml")
public class UserServiceTest {

    @Autowired
    private IUserService userService;


    @Test
    public void userSaveTest() {

        User user = new User();
        user.setUsername("ekocbiyik3");
        user.setPassword("61");
//        user.setCompany(0);
        user.setUserRole(EUserRole.ACCOUNT);
        user.setEmail("enbiya@com.tr");
        user.setFirstName("Enbiya");
        user.setLastName("Koçbıyık");
        user.setPhone("0123456789");
        user.setInActive(true);
        user.setCreatedBy("System");
        user.setCreationDate(new Date());

        userService.save(user);
    }

    @Test
    public void getAllsysadminTest() {

        List<User> allSysAdmins = userService.getAllSysAdmins();
        for (User u : allSysAdmins) {
            System.out.println(u.getUsername() + " - " + u.getUserRole());
        }

    }

    @Test
    public void getAlladminTest() {

        List<User> allAdmins = userService.getAllAdmins();
        for (User u : allAdmins) {
            System.out.println(u.getUsername() + " - " + u.getUserRole());
        }

    }

    @Test
    public void getAllaccountsTest() {

        List<User> allAccounts = userService.getAllAccountants();
        for (User u : allAccounts) {
            System.out.println(u.getUsername() + " - " + u.getUserRole());
        }

    }

    @Test
    public void userLoginTest() {

        User u = userService.login("ekocbiyik3", "61");

        if (u == null) {
            Assert.assertNotNull(u);

        } else {

            Assert.assertTrue(u.getFirstName() + " login oldu..", true);

        }


    }
}
