package com.alliance.radish.domain;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * 索引名称：user
 * 默认类型：_doc
 */
@Data
@Document(indexName = "user")
public class User implements Serializable {

    private String id;

    private String userName;

    private String age;

    private String address;
}
