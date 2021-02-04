package com.szcinda.express.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BugRejectDto implements Serializable {
    private String bugId;
    private String userId;
    private String remark;
}
