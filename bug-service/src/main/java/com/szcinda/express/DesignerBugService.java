package com.szcinda.express;

import com.szcinda.express.dto.*;
import com.szcinda.express.params.BugQueryParams;
import com.szcinda.express.params.PageResult;
import com.szcinda.express.persistence.BugType;
import com.szcinda.express.persistence.DesignerBug;

import java.util.List;

public interface DesignerBugService {

    void create(BugCreateDto createDto);

    List<DesignerBug> countByUNRESOLVED(BugType type);

    List<DesignerBug> countMyJobByUNRESOLVED(String userId);

    void assign(BugAssignDto assignDto);

    void reAssign(BugAssignDto assignDto);

    List<DesignerBug> countByWeekFinished(BugType type);

    List<DesignerBug> countMyJobByWeekFinished(String userId);

    PageResult<DesignerBug> query(BugQueryParams params, BugType type);

    void solved(BugSolvedDto bugSolvedDto);

    void finished(BugFinishedDto finishedDto);

    void reject(BugRejectDto rejectDto);

    void cancel(BugCancelDto cancelDto);

    BugDetailDto findById(String id);

    void publish(BugPublishDto publishDto);
}
