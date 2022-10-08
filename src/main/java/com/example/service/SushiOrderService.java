package com.example.service;

import com.example.mapper.OrderMapper;
import com.example.model.SushiOrder;
import com.example.repository.SushiOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SushiOrderService {

    @Autowired
    SushiOrderRepository sushiOrderRepository;

    public List<SushiOrder> getAllOrder() {
        List<SushiOrder> orders = new ArrayList<SushiOrder>();
        sushiOrderRepository.findAll().forEach(SushiOrder -> orders.add(SushiOrder));
        return orders;
    }

    public SushiOrder getOrderById(int id){
        return sushiOrderRepository.findById(id).get();
    }

    public void submitOrder(String name){
        //int id =
        SushiOrder order = new SushiOrder();
        order.setStatus_id(1);
        sushiOrderRepository.save(order);
    }

    public void cancelOrder(int id){
        //SushiOrder order = new
    }

    public void pauseOrder(int id){

    }

    public void resumeOrder(int id){

    }

}
