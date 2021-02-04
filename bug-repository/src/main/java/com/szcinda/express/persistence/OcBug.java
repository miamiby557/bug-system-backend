package com.szcinda.express.persistence;

import com.szcinda.express.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class OcBug extends BaseEntity {
    private String ocVersion;
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
}
