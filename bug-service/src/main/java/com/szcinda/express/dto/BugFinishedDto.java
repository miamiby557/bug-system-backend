package com.szcinda.express.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BugFinishedDto implements Serializable {
    private String bugId;
    private String reviewUserId;
    private String reviewRemark;
}
