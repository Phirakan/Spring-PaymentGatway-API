package com.mosuuuutech.CRUD.API.entity;

import jakarta.persistence.*;

@Entity
@Table (name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pid")
    private  int pid;

    @Column(name = "pnum")
    private  int pnum;

    @Column(name = "pname")
    private  String pname;

    @Column(name = "pdiscription")
    private  String pdiscription;

    @Column(name = "pprice")
    private  int pprice;

    public Product() {

    }

    public Product(int pnum, String pname, String pdiscription, int pprice) {
        this.pnum = pnum;
        this.pname = pname;
        this.pdiscription = pdiscription;
        this.pprice = pprice;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getPnum() {
        return pnum;
    }

    public void setPnum(int pnum) {
        this.pnum = pnum;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPdiscription() {
        return pdiscription;
    }

    public void setPdiscription(String pdiscription) {
        this.pdiscription = pdiscription;
    }

    public int getPprice() {
        return pprice;
    }

    public void setPprice(int pprice) {
        this.pprice = pprice;
    }

    @Override
    public String toString() {
        return "Product{" +
                "pid=" + pid +
                ", pnum=" + pnum +
                ", pname='" + pname + '\'' +
                ", pdiscription='" + pdiscription + '\'' +
                ", pprice=" + pprice +
                '}';
    }
}
