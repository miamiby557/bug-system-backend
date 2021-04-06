package com.szcinda.express.persistence;

import com.szcinda.express.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class DesignerBug extends BaseEntity {
    private String designerVersion; // 问题版本
    private String funcId; // 功能点ID
    private String publishVersion; // 发布版本号
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
    @Enumerated(EnumType.STRING)
    private BugType type;

    private String fixStatus; // 修复状态
    private String bugLevel;  // 紧急程度

    public DesignerBug() {
        this.fixStatus = BugStatus.NO_TEST;
    }
}
