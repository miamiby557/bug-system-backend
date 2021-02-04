package com.szcinda.express.dto;

import com.szcinda.express.persistence.BugType;
import lombok.Data;

import java.io.Serializable;

@Data
public class BugCreateDto implements Serializable {
    private String designerVersion;
    private String title;
    private String userId;
    private String remark;
    private String bugLevel;
    private BugType type;
}
