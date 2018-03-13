package com.taoding.controller.demo;

import com.taoding.domain.demo.Hotel;
import com.taoding.service.demo.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DemoController {

    @Autowired
    HotelService hotelService;

    @GetMapping("/hotel/{hotelId}")
    public Object getHotelInfo(@PathVariable("hotelId") Integer hotelId) {
        return hotelService.getHotel(hotelId);
    }

    @PostMapping("/hotel")
    public Object saveHotel(@RequestBody Hotel hotel) {
        return hotelService.saveHotel(hotel);
    }
    
    @GetMapping("/ceshi")
    public void ceshi(String message) {
//    	return "ceshi";
    }
    
}
