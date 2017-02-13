package com.ekocbiyik.dao;

import com.ekocbiyik.model.Office;
import com.ekocbiyik.model.User;

import java.util.List;

/**
 * Created by enbiya on 30.01.2017.
 */
public interface IOfficeDao {

    void save(Office office);

    void delete(Office office);

    List<Office> getAllOffices(User user);//user'a ait officeleri getir

}
