package com.szcinda.express.params;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageParams implements Serializable {
    private int page = 1;
    private int pageSize = 50;
    private String sort;
}
