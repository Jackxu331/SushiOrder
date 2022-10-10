package com.example.sushiorder.controller;

import com.example.sushiorder.model.SushiOrder;
import com.example.sushiorder.service.SushiOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SushiOrderController {

    @Autowired
    SushiOrderService sushiOrderService;

    @GetMapping("/orders/status")
    private List<SushiOrder> getAllOrder(){
        return sushiOrderService.getAllOrder();
    }

    @PostMapping("/order")
    private JsonResult submitOrder(@RequestBody String name){
        JsonResult result;
        SushiOrder order = sushiOrderService.submitOrder(name);
        if(order != null){
            result = new JsonResult(order,0,"Order created");
        }
        else{
            result = new JsonResult(1,"No Such Option");
        }
        return result;
    }

    @DeleteMapping("/orders/{order_id}")
    private JsonResult cancelOrder(int id){
        JsonResult result;
        if(sushiOrderService.cancelOrder(id)){
            result = new JsonResult(0,"order canceled");
        }
        else{
            result = new JsonResult(1,"No Such order");
        }
        return result;
    }

    @PutMapping("orders/{order_id}/pause")
    private JsonResult pauseOrder(int id){
        JsonResult result;
        if(sushiOrderService.cancelOrder(id)){
            result = new JsonResult(0,"order paused");
        }
        else{
            result = new JsonResult(1,"No Such order");
        }
        return result;
    }

    @PutMapping("orders/{order_id}/resume")
    private JsonResult resumeOrder(int id){
        JsonResult result;
        if(sushiOrderService.cancelOrder(id)){
            result = new JsonResult(0,"order resumed");
        }
        else{
            result = new JsonResult(1,"No Such order");
        }
        return result;
    }
}
