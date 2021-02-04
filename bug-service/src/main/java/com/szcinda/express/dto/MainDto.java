package com.szcinda.express.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class MainDto implements Serializable {

    private long designerBugCount; // 所有设计器未解决的bug
    private long designerBugCountEmergency; // 所有设计器未解决的bug 紧急
    private long executorBugCount; // 所有执行器未解决的bug
    private long executorBugCountEmergency; // 所有执行器未解决的bug 紧急
    private long ocBugCount; // 所有OC未解决的bug
    private long ocBugCountEmergency; // 所有OC未解决的bug 紧急

    private long myBugCount; // 关于我的bug
    private long myBugCountEmergency; // 关于我的bug 紧急
    private long myBugCountDesigner; // 关于我的bug
    private long myBugCountExecutor; // 关于我的bug
    private long myBugCountOc; // 关于我的bug

    private long myBugWeekFinishedCount; // 关于我这周修复的BUG
    private long myBugWeekFinishedCountEmergency; // 关于我这周修复的BUG 紧急
    private long myBugWeekFinishedCountDesigner; // 关于我这周修复的BUG
    private long myBugWeekFinishedCountExecutor; // 关于我这周修复的BUG
    private long myBugWeekFinishedCountOc; // 关于我这周修复的BUG

    private long designerWeekSolvedCount; // 今周设计器解决的bug
    private long designerWeekSolvedCountEmergency; // 今周设计器解决的bug
    private long executorWeekSolvedCount; // 今周执行器解决的bug
    private long executorWeekSolvedCountEmergency; // 今周执行器解决的bug
    private long ocWeekSolvedCount; // 今周OC解决的bug
    private long ocWeekSolvedCountEmergency; // 今周OC解决的bug
}
