package com.szcinda.express.persistence;

import lombok.Data;

@Data
public class BugStatus {
    public static String UNASSIGNED = "任务待指派";
    public static String UNRESOLVED = "开发待解决";
    public static String HANDLED = "开发已处理";
    public static String PUBLISHED = "开发已发布";
    public static String CLOSED = "问题已关闭";

    // 解决状态
    public static String NO_TEST = "未验证";
    public static String TESTED = "验证已通过";
    public static String FAILED = "验证不通过";

    // 紧急状态
    public static String USUALLY = "普通";
    public static String EMERGENCY = "紧急";
    public static String SUGGESTION = "建议";
}
