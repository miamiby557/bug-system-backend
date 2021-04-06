package com.szcinda.express.dto;

import com.szcinda.express.persistence.BugLog;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BugDetailDto implements Serializable {
    private String id;
    private String designerVersion;
    private String funcId; // 功能点ID
    private LocalDateTime createTime;
    private String title;
    private String userId;
    private String userName;
    private String remark;
    private String bugImage;
    private String reviewUserId;
    private String reviewUserName;
    private String reviewRemark;
    private LocalDateTime reviewTime;
    private String bugStatus;

    private List<BugLog> logList;
}
