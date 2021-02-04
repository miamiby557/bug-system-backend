package com.szcinda.express.params;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class BugQueryParams extends PageParams {
    private String version;
    private String publishVersion;
    private String title;
    private String userId;
    private String bugStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private String fixStatus;
}
