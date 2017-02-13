package model;

import com.ekocbiyik.model.Company;
import com.ekocbiyik.service.ICompanyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * Created by enbiya on 05.02.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:hibernate-config.xml")
public class CompanyServiceTest {

    @Autowired
    private ICompanyService companyService;

    @Test
    public void testSaveCompany() {

        Company c = new Company();
        c.setCompanyName("FirstCompany");
        c.setEmail("firstC@mail.com");
        c.setPhone("0123456789");
        c.setLocation("Trabzon");
        c.setCreationDate(new Date());
        c.setExpireDate(new Date(2017, 6, 1));
        c.setInActive(true);
        c.setDescription("burası açıklama...");

        companyService.save(c);

    }


}
