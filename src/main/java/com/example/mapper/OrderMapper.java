package com.example.mapper;

import com.example.model.SushiOrder;

import java.util.List;

public interface OrderMapper {

    public List<SushiOrder> findAll();

    public SushiOrder findOrder(int id);

    public void submitOrder(SushiOrder order);

    public void cancelOrder(int id);

    public void updateOrder(SushiOrder order);

    public void pauseOrder(int id);

    public void resumeOrder(int id);
}
