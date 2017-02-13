package com.ekocbiyik.dao;

import com.ekocbiyik.model.Office;
import com.ekocbiyik.model.ResourceRecords;
import com.ekocbiyik.model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by enbiya on 30.01.2017.
 */
@Component
public class ResourceRecordsDaoImpl implements IResourceRecordsDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(ResourceRecords resourceRecord) {
        getCurrentSession().saveOrUpdate(resourceRecord);
    }

    @Override
    public List<ResourceRecords> getResourceRecordsBySerialNumber(String serialNumber, User user) {
        Query q = getCurrentSession().createQuery("from ResourceRecords where serialNumber = :serialNumber and company = :company");
        q.setParameter("serialNumber", serialNumber);
        q.setParameter("company", user.getCompany());
        return q.list();
    }

    @Override
    public List<ResourceRecords> getAllResourceRecordsByOffice(Office office, Date startDate, Date endDate) {

        Calendar calStart = Calendar.getInstance();
        calStart.setTime(startDate);
        calStart.set(Calendar.HOUR_OF_DAY, 0);
        calStart.set(Calendar.MINUTE, 0);
        calStart.set(Calendar.SECOND, 0);
        calStart.set(Calendar.MILLISECOND, 0);

        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(endDate);
        calEnd.set(Calendar.HOUR_OF_DAY, 0);
        calEnd.set(Calendar.MINUTE, 0);
        calEnd.set(Calendar.SECOND, 0);
        calEnd.set(Calendar.MILLISECOND, 0);


        Query q = getCurrentSession().createQuery("from ResourceRecords where office = :office and company = :company and recordDate between :startDate and :endDate " +
                "order by recordDate, serialNumber desc");
        q.setParameter("office", office);
        q.setParameter("company", office.getCompany());
        q.setParameter("startDate", calStart.getTime());
        q.setParameter("endDate", calEnd.getTime());

        return q.list();
    }
}
