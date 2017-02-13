package com.ekocbiyik.dao;

import com.ekocbiyik.model.Company;

import java.util.List;

/**
 * Created by enbiya on 30.01.2017.
 */
public interface ICompanyDao {

    void save(Company company);

    void delete(Company company);

    List<Company> getAllCompanies();

}
