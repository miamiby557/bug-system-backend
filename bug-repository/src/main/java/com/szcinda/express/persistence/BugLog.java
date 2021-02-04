package com.szcinda.express.persistence;

import com.szcinda.express.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class BugLog extends BaseEntity {
    private String bugId;
    private String userId;
    private String userName;
    private String operation;
}
