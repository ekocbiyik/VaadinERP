package com.ekocbiyik.service;

import com.ekocbiyik.dao.IResourceRecordsDao;
import com.ekocbiyik.model.Office;
import com.ekocbiyik.model.ResourceRecords;
import com.ekocbiyik.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by enbiya on 30.01.2017.
 */
@Service
public class ResourceRecordServiceImpl implements IResourceRecordService {

    @Autowired
    private IResourceRecordsDao resourceRecordsDao;

    @Transactional
    @Override
    public void save(ResourceRecords resourceRecord) {
        resourceRecordsDao.save(resourceRecord);
    }

    @Transactional
    @Override
    public List<ResourceRecords> getResourceRecordsBySerialNumber(String serialNumber, User user) {
        return resourceRecordsDao.getResourceRecordsBySerialNumber(serialNumber, user);
    }

    @Transactional
    @Override
    public List<ResourceRecords> getAllResourceRecordsByOffice(Office office, Date startDate, Date endDate) {
        return resourceRecordsDao.getAllResourceRecordsByOffice(office, startDate, endDate);
    }
}
