package com.example.sushiorder.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class SushiOrder {

    @Id
    @Column
    private int id;
    @Column(name = "sushi_id")
    private int sushiId;
    @Column(name = "status_id")
    private int statusId;
    @Column(name = "created_at")
    private Date createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSushiId() {
        return sushiId;
    }

    public void setSushiId(int sushiId) {
        this.sushiId = sushiId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
