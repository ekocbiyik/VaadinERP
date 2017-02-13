package com.ekocbiyik.dao;

import com.ekocbiyik.enums.EUserRole;
import com.ekocbiyik.model.Company;
import com.ekocbiyik.model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by enbiya on 30.01.2017.
 */
@Component
public class UserDaoImpl implements IUserDao {

    /**
     * get**Users olan metotlarda o anki kullanıcının ait olduğu company deki userları listelettir!!!
     */

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(User user) {
        getCurrentSession().saveOrUpdate(user);
    }

    @Override
    public void delete(User user) {
        /**kullanıcı silme olmayacak*/
        getCurrentSession().delete(user);
    }

    @Override
    public User login(String username, String password) {

        /** user & pass hatalı ise null dönsün */

        Query q = getCurrentSession().createQuery("from User where username = :username and password = :password and inActive = true");
        q.setParameter("username", username);
        q.setParameter("password", password);

        List user = q.list();
        if (user.size() == 0) {
            return null;
        } else {

            return (User) user.get(0);
        }

    }

    @Override
    public List<User> getAllSysAdmins() {

        Query q = getCurrentSession().createQuery("from User where userRole = :role");
        q.setParameter("role", EUserRole.SYS_ADMIN);

        return q.list();
    }

    @Override
    public List<User> getAllAdmins() {

        Query q = getCurrentSession().createQuery("from User where userRole = :role");
        q.setParameter("role", EUserRole.ADMIN);

        return q.list();
    }

    @Override
    public List<User> getAllAccountants() {

        Query q = getCurrentSession().createQuery("from User where userRole = :role");
        q.setParameter("role", EUserRole.ACCOUNT);

        return q.list();
    }

    @Override
    public List<User> getAccountantsByCompany(Company company) {

        Query q = getCurrentSession().createQuery("from User where company = :company and userRole = :role");
        q.setParameter("company", company);
        q.setParameter("role", EUserRole.ACCOUNT);

        return q.list();
    }
}
