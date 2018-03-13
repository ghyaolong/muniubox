package com.taoding.service.demo;

import com.taoding.domain.demo.Hotel;

public interface HotelService {

    public Hotel getHotel(int id);

    public boolean saveHotel(Hotel hotel);
}
