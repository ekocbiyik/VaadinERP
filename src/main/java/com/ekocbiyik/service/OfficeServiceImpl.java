package com.ekocbiyik.service;

import com.ekocbiyik.dao.IOfficeDao;
import com.ekocbiyik.model.Office;
import com.ekocbiyik.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by enbiya on 30.01.2017.
 */
@Service
public class OfficeServiceImpl implements IOfficeService {

    @Autowired
    private IOfficeDao officeDao;

    @Transactional
    @Override
    public void save(Office office) {
        officeDao.save(office);
    }

    @Transactional
    @Override
    public void delete(Office office) {
        officeDao.delete(office);
    }

    @Transactional
    @Override
    public List<Office> getAllOffices(User user) {
        return officeDao.getAllOffices(user);
    }
}
