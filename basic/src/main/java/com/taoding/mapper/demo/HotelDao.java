package com.taoding.mapper.demo;

import com.taoding.domain.demo.Hotel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface HotelDao {

    Hotel selectByCityId(int city_id);

    int insertHotel(Hotel hotel);
}
