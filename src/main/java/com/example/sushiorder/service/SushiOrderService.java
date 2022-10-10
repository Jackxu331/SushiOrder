package com.example.sushiorder.service;

import com.example.sushiorder.model.Status;
import com.example.sushiorder.model.Sushi;
import com.example.sushiorder.model.SushiOrder;
import com.example.sushiorder.repository.StatusRepository;
import com.example.sushiorder.repository.SushiOrderRepository;
import com.example.sushiorder.repository.SushiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

@Component
@Service
public class SushiOrderService {

    @Autowired
    SushiOrderRepository sushiOrderRepository;
    @Autowired
    SushiRepository sushiRepository;
    @Autowired
    StatusRepository statusRepository;

    public Map<Integer, ScheduledFuture<?>> taskMap = new ConcurrentHashMap<>();
    public List<Integer> taskList = new CopyOnWriteArrayList<Integer>();

    private final ThreadPoolTaskScheduler syncScheduler;

    public SushiOrderService(ThreadPoolTaskScheduler syncScheduler){
        this.syncScheduler = syncScheduler;
    }
    public List<SushiOrder> getAllOrder() {
        List<SushiOrder> orders = new ArrayList<SushiOrder>();
        sushiOrderRepository.findAll().forEach(SushiOrder -> orders.add(SushiOrder));
        return orders;
    }

    public SushiOrder submitOrder(String name){
        Date date = new Date();

        //if the order cant match the menu, reject the order
        if(name!=null&&name.length()>0) {
            Status status = statusRepository.findByName("created");
            Sushi sushi = sushiRepository.findByName("California Roll");

            int timeToMake = sushi.getTimeToMake();
            SushiOrder order = new SushiOrder();
            order.setStatusId(status.getId());
            order.setSushiId(sushi.getId());
            order.setCreatedAt(date);
            sushiOrderRepository.save(order);
            return order;
        }
        else
            return null;
    }

    public Runnable processOrder(SushiOrder order){
        return() -> {
            try{

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public boolean cancelOrder(int id){
        Optional<SushiOrder> order = sushiOrderRepository.findById(id);

        //if order_id does not exist, return false
        if(order != null){
            Status status = statusRepository.findByName("cancelled");
            order.get().setStatusId(status.getId());
            sushiOrderRepository.save(order.get());
            return true;
        }
        else{
            return false;
        }
    }

    public boolean pauseOrder(int id){
        Optional<SushiOrder> order = sushiOrderRepository.findById(id);
        if(order != null){
            Status status = statusRepository.findByName("paused");
            order.get().setStatusId(status.getId());
            sushiOrderRepository.save(order.get());
            return true;
        }
        else{
            return false;
        }
    }

    public boolean resumeOrder(int id){
        Optional<SushiOrder> order = sushiOrderRepository.findById(id);
        Status status = statusRepository.findByName("paused");
        if(order.get().getStatusId() == status.getId() ){
            status = statusRepository.findByName("created");
            order.get().setStatusId(status.getId());
            sushiOrderRepository.save(order.get());
            return true;
        }
        else{
            return false;
        }
    }

    public int timeLeft(int id){
        Optional<SushiOrder> order = sushiOrderRepository.findById(id);
        long nowDate = new Date().getTime();
        long startTime = order.get().getCreatedAt().getTime();
        return (int) ((nowDate - startTime) / 1000);
    }

}
