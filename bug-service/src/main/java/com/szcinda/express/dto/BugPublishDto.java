package com.szcinda.express.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BugPublishDto implements Serializable {
    private List<String> bugIds;
    private String userId;
    private String publishVersion;
}
