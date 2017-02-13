package com.ekocbiyik.dao;

import com.ekocbiyik.model.Company;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by enbiya on 30.01.2017.
 */
@Component
public class CompanyDaoImpl implements ICompanyDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(Company company) {
        getCurrentSession().saveOrUpdate(company);
    }

    @Override
    public void delete(Company company) {
        getCurrentSession().delete(company);
    }

    @Override
    public List<Company> getAllCompanies() {
        return getCurrentSession().createQuery("from Company").list();
    }
}
