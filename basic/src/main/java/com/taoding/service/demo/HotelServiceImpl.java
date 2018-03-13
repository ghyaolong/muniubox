package com.taoding.service.demo;

import com.taoding.mapper.demo.CityDao;
import com.taoding.mapper.demo.HotelDao;
import com.taoding.common.exception.LogicException;
import com.taoding.domain.demo.City;
import com.taoding.domain.demo.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HotelServiceImpl implements HotelService {
	
	@Autowired
    HotelDao hotelDao;
    
	@Autowired
    CityDao cityDao;

    @Override
    @Transactional
    public Hotel getHotel(int id) {
        return hotelDao.selectByCityId(id);
    }

    @Override
//    @Transactional(noRollbackFor = LogicException.class)
    @Transactional
    public boolean saveHotel(Hotel hotel) {
    	City city = new City();
    	city.setCountry("china");
    	city.setName("xi'an");
    	city.setState("shaanxi");
    	cityDao.insertCity(city);
        int returnValue = hotelDao.insertHotel(hotel);
        throw new LogicException("eeeee");
//        return returnValue > 0;
    }
}
