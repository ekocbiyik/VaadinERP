package com.ekocbiyik.service;

import com.ekocbiyik.model.Office;
import com.ekocbiyik.model.ResourceRecords;
import com.ekocbiyik.model.User;

import java.util.Date;
import java.util.List;

/**
 * Created by enbiya on 30.01.2017.
 */
public interface IResourceRecordService {

    void save(ResourceRecords resourceRecord);

    List<ResourceRecords> getResourceRecordsBySerialNumber(String serialNumber, User user);

    List<ResourceRecords> getAllResourceRecordsByOffice(Office office, Date startDate, Date endDate);

}
