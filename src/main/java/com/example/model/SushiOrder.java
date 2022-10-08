package com.example.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

public class SushiOrder {

    @Id
    @Column
    private int id;
    @Column
    private int sushi_id;
    @Column
    private int status_id;
    @Column
    private Date created_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSushi_id() {
        return sushi_id;
    }

    public void setSushi_id(int sushi_id) {
        this.sushi_id = sushi_id;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

}
