package com.taoding.domain.ticket;

import java.util.Date;

import lombok.Data;

@Data
public class ListTemplate {
	// 主键
    private String id;
    // 关联的表名称
    private String tableName;
    // 模板数据, 为json字符串导入时需转换成对象,插入到关联的表中
    private String templateList;
    // 版本号
    private String version;
    // 是否推荐更新
    private Byte recommendUpdate;
    // 是否为过期版本
    private Byte expiredVersion;
    // 创建时间
    private Date created;
    // 更新时间
    private Date updated;
}