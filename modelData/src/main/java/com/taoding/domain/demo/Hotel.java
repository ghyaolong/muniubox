package com.taoding.domain.demo;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import com.taoding.common.entity.TreeEntity;
import com.taoding.domain.office.Office;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@ToString
@ValidationBean
@EqualsAndHashCode(callSuper=false)
public class Hotel extends DataEntity<Hotel> {

    @NotNull(message = "城市不能为空")
    private Long city;

    @NotEmpty(message = "名称不能为空")
    private String name;

    @NotEmpty(message = "地址不能为空")
    private String address;

    @NotEmpty(message = "邮政编码不能为空")
    private String zip;

}
