package com.taoding.domain.demo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class City {

    private Long id;

    private String name;

    private String state;

    private String country;

}
