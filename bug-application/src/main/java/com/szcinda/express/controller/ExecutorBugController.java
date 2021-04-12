package com.szcinda.express.controller;

import com.szcinda.express.DesignerBugService;
import com.szcinda.express.UserService;
import com.szcinda.express.configuration.UserLoginToken;
import com.szcinda.express.controller.dto.Result;
import com.szcinda.express.dto.*;
import com.szcinda.express.params.BugQueryParams;
import com.szcinda.express.params.PageResult;
import com.szcinda.express.params.QueryUserParams;
import com.szcinda.express.persistence.BugType;
import com.szcinda.express.persistence.DesignerBug;
import com.szcinda.express.persistence.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/executorBug")
public class ExecutorBugController {

    private final UserService userService;
    private final DesignerBugService designerBugService;

    public ExecutorBugController(UserService userService, DesignerBugService designerBugService) {
        this.userService = userService;
        this.designerBugService = designerBugService;
    }

    @UserLoginToken
    @PostMapping("/query")
    public PageResult<DesignerBug> query(@RequestBody BugQueryParams params) {
        return designerBugService.query(params, BugType.EXECUTOR);
    }

    @PostMapping("create")
    public Result<String> createBug(@RequestBody BugCreateDto createDto){
        createDto.setType(BugType.EXECUTOR);
        designerBugService.create(createDto);
        return Result.success();
    }

    @PostMapping("solve")
    public Result<String> solve(@RequestBody BugSolvedDto solvedDto){
        designerBugService.solved(solvedDto);
        return Result.success();
    }

    @PostMapping("finish")
    public Result<String> finish(@RequestBody BugFinishedDto finishedDto){
        designerBugService.finished(finishedDto);
        return Result.success();
    }

    @PostMapping("publish")
    public Result<String> publish(@RequestBody BugPublishDto publishDto){
        designerBugService.publish(publishDto);
        return Result.success();
    }

    @PostMapping("reject")
    public Result<String> reject(@RequestBody BugRejectDto rejectDto){
        designerBugService.reject(rejectDto);
        return Result.success();
    }


    @PostMapping("cancel")
    public Result<String> cancel(@RequestBody BugCancelDto cancelDto){
        designerBugService.cancel(cancelDto);
        return Result.success();
    }

    @PostMapping("assign")
    public Result<String> assign(@RequestBody BugAssignDto assignDto){
        designerBugService.assign(assignDto);
        return Result.success();
    }

    @PostMapping("reAssign")
    public Result<String> reAssign(@RequestBody BugAssignDto assignDto){
        designerBugService.reAssign(assignDto);
        return Result.success();
    }

    @PostMapping("batchAssign")
    public Result<String> batchAssign(@RequestBody BugAssignDto assignDto){
        designerBugService.batchAssign(assignDto);
        return Result.success();
    }

    @GetMapping("getById/{bugId}")
    public Result<BugDetailDto> getById(@PathVariable String bugId){
        return Result.success(designerBugService.findById(bugId));
    }
}
