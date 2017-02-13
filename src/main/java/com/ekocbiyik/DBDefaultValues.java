package com.ekocbiyik;

import com.ekocbiyik.enums.EUserRole;
import com.ekocbiyik.model.Company;
import com.ekocbiyik.model.Office;
import com.ekocbiyik.model.ResourceRecords;
import com.ekocbiyik.model.User;
import com.ekocbiyik.service.ICompanyService;
import com.ekocbiyik.service.IOfficeService;
import com.ekocbiyik.service.IResourceRecordService;
import com.ekocbiyik.service.IUserService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by enbiya on 06.01.2017.
 */
public class DBDefaultValues {


    public static String[] iller = {"Adana", "Adıyaman", "Afyon", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin", "Aydın", "Balıkesir", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa",
            "Çanakkale", "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Edirne", "Elazığ", "Erzincan", "Erzurum", "Eskişehir", "Gaziantep", "Giresun", "Gümüşhane", "Hakkari", "Hatay", "Isparta",
            "Mersin", "İstanbul", "İzmir", "Kars", "Kastamonu", "Kayseri", "Kırklareli", "Kırşehir", "Kocaeli", "Konya", "Kütahya", "Malatya", "Manisa", "Kahramanmaraş", "Mardin", "Muğla", "Muş",
            "Nevşehir", "Niğde", "Ordu", "Rize", "Sakarya", "Samsun", "Siirt", "Sinop", "Sivas", "Tekirdağ", "Tokat", "Trabzon", "Tunceli", "Şanlıurfa", "Uşak", "Van", "Yozgat", "Zonguldak",
            "Aksaray", "Bayburt", "Karaman", "Kırıkkale", "Batman", "Şırnak", "Bartın", "Ardahan", "Iğdır", "Yalova", "Karabük", "Kilis", "Osmaniye", "Düzce"};

    public static void initialize() {

        addDefaultCompany();
        addDefaultUsers();
        addDefaultOffice();
        addDefaultResourceRecords();

    }

    private static void addDefaultCompany() {

        ICompanyService companyService = UtilsForSpring.getSingleBeanOfType(ICompanyService.class);

        if (companyService.getAllCompanies().size() == 0) {

            for (int i = 0; i < 3; i++) {
                Company c = new Company();
                c.setCompanyName("Company-" + i);
                c.setEmail(i + "-company@mail.com");
                c.setPhone("0123456789");
                c.setLocation(iller[60]);
                c.setEmail(i + "cmp@mail.com");
                c.setCreationDate(new Date());
                c.setExpireDate((new GregorianCalendar(2018, Calendar.AUGUST, 4)).getTime());
                c.setInActive(true);
                c.setDescription(i + "-Company");

                companyService.save(c);
            }
        }

    }

    private static void addDefaultUsers() {

        IUserService userService = UtilsForSpring.getSingleBeanOfType(IUserService.class);

        ICompanyService companyService = UtilsForSpring.getSingleBeanOfType(ICompanyService.class);

        /**db de kullanıcı yoksa */
        if (userService.getAllSysAdmins().size() < 1) {

            User admin = new User();
            admin.setUsername("sysadmin");
            admin.setPassword("sysadmin");
            admin.setUserRole(EUserRole.SYS_ADMIN);
            admin.setFirstName("System");
            admin.setLastName("System");
            admin.setEmail("system@mail.com");
            admin.setPhone("1234567890");
            admin.setCreationDate(new Date());
            admin.setCreatedBy("System");
            admin.setInActive(true);

            userService.save(admin);
        }

        if (userService.getAllAdmins().size() < 1) {

            for (int i = 0; i < 3; i++) {

                User test = new User();
                test.setUsername("admin" + i);
                test.setPassword("admin" + i);
                test.setUserRole(EUserRole.ADMIN);
                test.setFirstName("admin" + i);
                test.setLastName("admin" + i);
                test.setEmail("admin@mail.com");
                test.setPhone("1234567890");
                test.setCreationDate(new Date());
                test.setCreatedBy("System");
                test.setInActive(true);
                test.setCompany(companyService.getAllCompanies().get(i));

                userService.save(test);
            }
        }

        if (userService.getAllAccountants().size() < 1) {

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 2; j++) {

                    User test = new User();
                    test.setUsername("account" + i + j);
                    test.setPassword("account" + i + j);
                    test.setUserRole(EUserRole.ACCOUNT);
                    test.setFirstName("account" + i + j);
                    test.setLastName("account" + i + j);
                    test.setEmail("account@mail.com");
                    test.setPhone("1234567890");
                    test.setCreationDate(new Date());
                    test.setCreatedBy("System");
                    test.setInActive(true);
                    test.setCompany(companyService.getAllCompanies().get(i));

                    userService.save(test);
                }
            }
        }

    }

    private static void addDefaultOffice() {

        ICompanyService companyService = UtilsForSpring.getSingleBeanOfType(ICompanyService.class);
        IUserService userService = UtilsForSpring.getSingleBeanOfType(IUserService.class);
        IOfficeService officeService = UtilsForSpring.getSingleBeanOfType(IOfficeService.class);

        if (officeService.getAllOffices(userService.getAllSysAdmins().get(0)).size() < 1) {

            for (Company c : companyService.getAllCompanies()) {

                for (User u : userService.getAccountantsByCompany(c)) {

                    int index = 0;
                    for (int i = 0; i < 3; i++) {

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSSS");

                        Office o = new Office();
                        o.setOfficeId(c.getCompanyID() + sdf.format(new Date()));//companyid+yyyyMMddHHmmSSS
                        o.setOfficeName(c.getCompanyName() + "-Account" + u.getUserId() + "-office" + index++);
                        o.setCompany(c);
                        o.setEmail("mail@mail.com");
                        o.setPhone("123456789");
                        o.setOwner(u);
                        o.setLocation(iller[33]);
                        o.setInActive(true);

                        officeService.save(o);

                        try {
                            TimeUnit.MILLISECONDS.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private static void addDefaultResourceRecords() {

        User u = new User();
        u.setUserRole(EUserRole.SYS_ADMIN);
        List<Office> officeList = UtilsForSpring.getSingleBeanOfType(IOfficeService.class).getAllOffices(u);

        IResourceRecordService recordService = UtilsForSpring.getSingleBeanOfType(IResourceRecordService.class);

        if (false) {
            for (Office office : officeList) {

                for (int i = 10; i < 20; i++) {

                    ResourceRecords test = new ResourceRecords();

                    test.setRecordId(UUID.randomUUID().toString());
                    test.setOffice(office);
                    test.setCompany(office.getCompany());
                    test.setSerialNumber("20001100" + i + "-012"); // 12 hane olsun
                    test.setFieldBrand("şlkasdjfşlkasdf");
                    test.setFieldModel("lkjdhfkasdfads");
                    test.setFieldVersion("version001" + i);
                    test.setField1(true);
                    test.setField3(true);
                    test.setField4(true);
                    test.setField5(true);
                    test.setField6(true);
                    test.setField7(true);
                    test.setField8(true);
                    test.setField9(true);
                    test.setRecordDate(new Date());
                    test.setField10(i + ".kayıt: " + test.getRecordDate());

                    recordService.save(test);
                }

            }
        }


    }

}
