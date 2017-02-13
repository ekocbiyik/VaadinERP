package com.ekocbiyik.service;

import com.ekocbiyik.dao.ICompanyDao;
import com.ekocbiyik.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by enbiya on 30.01.2017.
 */
@Service
public class CompanyServiceImpl implements ICompanyService {

    @Autowired
    private ICompanyDao ccompanyDao;

    @Transactional
    @Override
    public void save(Company company) {
        ccompanyDao.save(company);
    }

    @Transactional
    @Override
    public void delete(Company company) {
        ccompanyDao.delete(company);
    }

    @Transactional
    @Override
    public List<Company> getAllCompanies() {
        return ccompanyDao.getAllCompanies();
    }
}
