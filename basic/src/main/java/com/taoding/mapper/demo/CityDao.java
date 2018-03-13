package com.taoding.mapper.demo;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.domain.demo.City;

@Repository
@Mapper
public interface CityDao {
	
	public City selectCityById(int id);
	
	public int insertCity(City city);

}
