package com.ekocbiyik.dao;

import com.ekocbiyik.model.Company;
import com.ekocbiyik.model.User;

import java.util.List;

/**
 * Created by enbiya on 30.01.2017.
 */
public interface IUserDao {

    void save(User user);

    void delete(User user);

    User login(String username, String password);

    List<User> getAllSysAdmins();

    List<User> getAllAdmins();

    List<User> getAllAccountants();

    List<User> getAccountantsByCompany(Company company);

}
