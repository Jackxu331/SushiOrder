package com.example.controller;

import com.example.model.SushiOrder;
import com.example.service.SushiOrderService;
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

    @PostMapping("/orders")
    private JsonResult submitOrder(String name){
        sushiOrderService.submitOrder(name);
        JsonResult result = new JsonResult(0,"Order created");
        return result;
    }

    @DeleteMapping("/orders/{order_id}")
    private JsonResult cancelOrder(int id){

        JsonResult result = new JsonResult();

        return result;
    }

    @PutMapping("orders/{order_id}/pause")
    private JsonResult pauseOrder(int id){
        JsonResult result = new JsonResult();

        return result;
    }

    @PutMapping("orders/{order_id}/resume")
    private JsonResult resumeOrder(int id){
        JsonResult result = new JsonResult();

        return result;
    }
}
