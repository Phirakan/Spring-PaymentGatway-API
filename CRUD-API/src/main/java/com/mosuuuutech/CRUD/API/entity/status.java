package com.mosuuuutech.CRUD.API.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "status")
public class status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int statusid;
    private String statusname;
    private Timestamp createat;
    private Timestamp updateat;

    public status() {

    }

    public status(String statusname, Timestamp createat, Timestamp updateat) {
        this.statusname = statusname;
        this.createat = createat;
        this.updateat = updateat;
    }

    public int getStatusid() {
        return statusid;
    }

    public void setStatusid(int statusid) {
        this.statusid = statusid;
    }

    public String getStatusname() {
        return statusname;
    }

    public void setStatusname(String statusname) {
        this.statusname = statusname;
    }

    public Timestamp getCreateat() {
        return createat;
    }

    public void setCreateat(Timestamp createat) {
        this.createat = createat;
    }

    public Timestamp getUpdateat() {
        return updateat;
    }

    public void setUpdateat(Timestamp updateat) {
        this.updateat = updateat;
    }

    @Override
    public String toString() {
        return "status{" +
                "statusid=" + statusid +
                ", statusname='" + statusname + '\'' +
                ", createat=" + createat +
                ", updateat=" + updateat +
                '}';
    }
}
