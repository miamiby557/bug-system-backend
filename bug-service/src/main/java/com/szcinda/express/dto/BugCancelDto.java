package com.szcinda.express.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BugCancelDto implements Serializable {
    private String bugId;
    private String userId;
}
