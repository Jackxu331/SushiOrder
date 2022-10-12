package com.example.sushiorder.controller;

import com.example.sushiorder.model.SushiOrder;
import com.example.sushiorder.model.Task;
import com.example.sushiorder.service.SushiOrderService;


import com.fasterxml.jackson.core.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

@RestController
public class SushiOrderController {

    @Autowired
    SushiOrderService sushiOrderService;

    @GetMapping("/orders/status")
    private Map<String,List<Task>> getAllOrder(){
        return sushiOrderService.getAllOrder();
    }

    @PostMapping("/order")
    private JsonResult submitOrder(@RequestBody String orderName) {
        JsonResult result;
        JSONObject obj;
        try {
                obj = new JSONObject(orderName);
                String name = obj.getString("sushi_name");
                SushiOrder order = sushiOrderService.submitOrder(name);
                if (order != null) {
                    result = new JsonResult(order, 0, "Order created");
                } else {
                    result = new JsonResult(1, "No Such Option");
                }
            } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @DeleteMapping(value="orders/{order_id}")
    private JsonResult cancelOrder(@PathVariable int order_id){
        JsonResult result;
        if(sushiOrderService.cancelOrder(order_id)){
            result = new JsonResult(0,"order canceled");
        }
        else{
            result = new JsonResult(1,"No Such order");
        }
        return result;
    }

    @PutMapping("orders/{order_id}/pause")
    private JsonResult pauseOrder(@PathVariable int order_id){
        JsonResult result;
        if(sushiOrderService.pauseOrder(order_id)){
            result = new JsonResult(0,"order paused");
        }
        else{
            result = new JsonResult(1,"No Such order");
        }
        return result;
    }

    @PutMapping("orders/{order_id}/resume")
    private JsonResult resumeOrder(@PathVariable int order_id){
        JsonResult result;
        if(sushiOrderService.resumeOrder(order_id)){
            result = new JsonResult(0,"order resumed");
        }
        else{
            result = new JsonResult(1,"No Such order");
        }
        return result;
    }
}
