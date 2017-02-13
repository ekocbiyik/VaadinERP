package com.ekocbiyik.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by enbiya on 29.01.2017.
 */
@Entity
@Table(name = "t_resource_records")
public class ResourceRecords {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "record_id", nullable = false)
    private String recordId;//bu zaten clienttan gelecek

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "office_id")
    private Office office;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    @Column(name = "field_brand", nullable = false)
    private String fieldBrand;

    @Column(name = "field_model", nullable = false)
    private String fieldModel;

    @Column(name = "field_version", nullable = false)
    private String fieldVersion;

    @Column(name = "field1", nullable = false)
    private boolean field1;

    @Column(name = "field2", nullable = false)
    private boolean field2;

    @Column(name = "field3", nullable = false)
    private boolean field3;

    @Column(name = "field4", nullable = false)
    private boolean field4;

    @Column(name = "field5", nullable = false)
    private boolean field5;

    @Column(name = "field6", nullable = false)
    private boolean field6;

    @Column(name = "field7", nullable = false)
    private boolean field7;

    @Column(name = "field8", nullable = false)
    private boolean field8;

    @Column(name = "field9", nullable = false)
    private boolean field9;

    @Column(name = "field10", nullable = false)
    private String field10;

    @Column(name = "record_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date recordDate;

    public int getId() {
        return id;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getFieldBrand() {
        return fieldBrand;
    }

    public void setFieldBrand(String fieldBrand) {
        this.fieldBrand = fieldBrand;
    }

    public String getFieldModel() {
        return fieldModel;
    }

    public void setFieldModel(String fieldModel) {
        this.fieldModel = fieldModel;
    }

    public String getFieldVersion() {
        return fieldVersion;
    }

    public void setFieldVersion(String fieldVersion) {
        this.fieldVersion = fieldVersion;
    }

    public boolean isField1() {
        return field1;
    }

    public void setField1(boolean field1) {
        this.field1 = field1;
    }

    public boolean isField2() {
        return field2;
    }

    public void setField2(boolean field2) {
        this.field2 = field2;
    }

    public boolean isField3() {
        return field3;
    }

    public void setField3(boolean field3) {
        this.field3 = field3;
    }

    public boolean isField4() {
        return field4;
    }

    public void setField4(boolean field4) {
        this.field4 = field4;
    }

    public boolean isField5() {
        return field5;
    }

    public void setField5(boolean field5) {
        this.field5 = field5;
    }

    public boolean isField6() {
        return field6;
    }

    public void setField6(boolean field6) {
        this.field6 = field6;
    }

    public boolean isField7() {
        return field7;
    }

    public void setField7(boolean field7) {
        this.field7 = field7;
    }

    public boolean isField8() {
        return field8;
    }

    public void setField8(boolean field8) {
        this.field8 = field8;
    }

    public boolean isField9() {
        return field9;
    }

    public void setField9(boolean field9) {
        this.field9 = field9;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public String getField10() {
        return field10;
    }

    public void setField10(String field10) {
        this.field10 = field10;
    }
}
