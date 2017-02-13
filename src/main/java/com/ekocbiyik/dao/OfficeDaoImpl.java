package com.ekocbiyik.dao;

import com.ekocbiyik.enums.EUserRole;
import com.ekocbiyik.model.Office;
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
public class OfficeDaoImpl implements IOfficeDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(Office office) {
        getCurrentSession().saveOrUpdate(office);
    }

    @Override
    public void delete(Office office) {
        getCurrentSession().delete(office);
    }

    @Override
    public List<Office> getAllOffices(User user) {

        if (user.getUserRole() == EUserRole.SYS_ADMIN) {
            return getCurrentSession().createQuery("from Office as o order by o.company asc").list();
        } else if (user.getUserRole() == EUserRole.ADMIN) {

            Query q = getCurrentSession().createQuery("from Office as o where o.company = :company order by o.company asc");
            q.setParameter("company", user.getCompany());
            return q.list();
        } else {

            Query q = getCurrentSession().createQuery("from Office as o where o.company = :company and o.owner = :user");
            q.setParameter("company", user.getCompany());
            q.setParameter("user", user);
            return q.list();
        }
    }
}
