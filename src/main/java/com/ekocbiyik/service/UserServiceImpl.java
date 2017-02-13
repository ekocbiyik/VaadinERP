package com.ekocbiyik.service;

import com.ekocbiyik.dao.IUserDao;
import com.ekocbiyik.model.Company;
import com.ekocbiyik.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by enbiya on 30.01.2017.
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserDao userDao;

    @Transactional
    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Transactional
    @Override
    public void delete(User user) {
        userDao.delete(user);
    }

    @Transactional
    @Override
    public User login(String username, String password) {
        return userDao.login(username, password);
    }

    @Transactional
    @Override
    public List<User> getAllSysAdmins() {
        return userDao.getAllSysAdmins();
    }

    @Transactional
    @Override
    public List<User> getAllAdmins() {
        return userDao.getAllAdmins();
    }

    @Transactional
    @Override
    public List<User> getAllAccountants() {
        return userDao.getAllAccountants();
    }

    @Transactional
    @Override
    public List<User> getAccountantsByCompany(Company company) {
        return userDao.getAccountantsByCompany(company);
    }
}
