package com.example.sushiorder.service;

import com.example.sushiorder.model.Status;
import com.example.sushiorder.model.Sushi;
import com.example.sushiorder.model.SushiOrder;
import com.example.sushiorder.model.Task;
import com.example.sushiorder.repository.StatusRepository;
import com.example.sushiorder.repository.SushiOrderRepository;
import com.example.sushiorder.repository.SushiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    public List<Task> taskList = new CopyOnWriteArrayList<>();

    private final ThreadPoolTaskScheduler syncScheduler;

    public SushiOrderService(ThreadPoolTaskScheduler syncScheduler){
        this.syncScheduler = syncScheduler;
    }
    public Map<String, List<Task>> getAllOrder() {
        Map<String , List<Task>> orders = taskList.stream().collect(Collectors.groupingBy(Task::getStatus));
        return orders;
    }

    public SushiOrder submitOrder(String name){
        Date date = new Date();

        //if the order cant match the menu, reject the order
        if(name!=null&&name.length()>0) {
            Status status = statusRepository.findByName("created");
            Sushi sushi = sushiRepository.findByName(name);

            //update submit order into database
            int timeToMake = sushi.getTimeToMake();
            SushiOrder order = new SushiOrder();
            order.setStatusId(status.getId());
            order.setSushiId(sushi.getId());
            order.setCreatedAt(date);
            sushiOrderRepository.save(order);

            //add task into schedule
            //also set task.orderId = order.id to so that able to pause/resume order
            Task task = new Task(order.getId());
            task.setStatus("created");
            ScheduledFuture<?> schedule = syncScheduler.schedule(processOrder(task,order), Instant.now());
            taskMap.put(task.getOrderId(), schedule);
            taskList.add(task);
            return order;
        }
        else
            return null;
    }

    public Runnable processOrder(Task task, SushiOrder order){
        return() -> {
            try{
                //get the time for order to finishe
                int time = sushiRepository.findById(order.getSushiId()).get().getTimeToMake() - task.getTimeSpent();

                //set order to in-progress
                int statusId = statusRepository.findByName("in-progress").getId();
                order.setStatusId(statusId);
                sushiOrderRepository.save(order);
                task.setStartTime(System.nanoTime());
                task.setStatus("in-progress");
                //order processing
                for(int i = 0; i < time; i++){
                    Thread.sleep(1000);
                    task.setTimeSpent(task.getTimeSpent() + 1);
                }
                //order finished
                statusId = statusRepository.findByName("finished").getId();
                task.setStatus("finished");
                order.setStatusId(statusId);
                sushiOrderRepository.save(order);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }


    public boolean cancelOrder(int id){
        Optional<SushiOrder> order = sushiOrderRepository.findById(id);

        //if order_id does not exist, return false
        if(order != null){

            //remove pausedOrder from schedule and add it into pausedList
            ScheduledFuture<?> scheduledFuture = taskMap.get(order.get().getId());
            try{
                scheduledFuture.cancel(true);
            }catch (Exception e) {
                Thread.currentThread().interrupt();
            }
            taskMap.remove(order.get().getId());

            //update timeSpent for tasks in taskList
            Task task = findFirstMatch(id);
            long now = System.nanoTime();
            int convert = (int) TimeUnit.SECONDS.convert(now - task.getStartTime(), TimeUnit.NANOSECONDS);
            task.setTimeSpent(task.getTimeSpent() + convert);
            task.setStatus("cancelled");

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

            //remove pausedOrder from schedule and add it into pausedList
            ScheduledFuture<?> scheduledFuture = taskMap.get(order.get().getId());
            try{
                scheduledFuture.cancel(true);
            }catch (Exception e) {
                Thread.currentThread().interrupt();
            }
            taskMap.remove(order.get().getId());

            //update timeSpent for tasks in taskList
            Task task = findFirstMatch(id);
            long now = System.nanoTime();
            int convert = (int) TimeUnit.SECONDS.convert(now - task.getStartTime(), TimeUnit.NANOSECONDS);
            task.setTimeSpent(task.getTimeSpent() + convert);
            task.setStatus("paused");

            //change order status in database
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

        //change status in database
        if(order!=null && order.get().getStatusId() == status.getId()){
            status = statusRepository.findByName("created");
            order.get().setStatusId(status.getId());
            sushiOrderRepository.save(order.get());

            //find paused order in taskList and add it into taskMap and schedule
            Task task = findFirstMatch(id);
            task.setStatus("created");
            ScheduledFuture<?> schedule = syncScheduler.schedule(processOrder(task,order.get()), Instant.now());
            taskMap.put(id,schedule);
            return true;
        }
        else{
            return false;
        }
    }

    //return first match in taskList
    public Task findFirstMatch(int id){
        for(Task t : taskList){
            if (t.getOrderId() == id)
                    return t;
        }
        return null;
    }
}
