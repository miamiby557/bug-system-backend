package com.szcinda.express;

import com.szcinda.express.dto.*;
import com.szcinda.express.params.BugQueryParams;
import com.szcinda.express.params.PageResult;
import com.szcinda.express.persistence.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
@Transactional
public class DesignerBugServiceImpl implements DesignerBugService {


    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DesignerBugRepository designerBugRepository;
    private final SnowFlakeFactory snowFlakeFactory;
    private final BugLogRepository bugLogRepository;
    private final UserRepository userRepository;

    public DesignerBugServiceImpl(DesignerBugRepository designerBugRepository, BugLogRepository bugLogRepository, UserRepository userRepository) {
        this.designerBugRepository = designerBugRepository;
        this.bugLogRepository = bugLogRepository;
        this.userRepository = userRepository;
        this.snowFlakeFactory = SnowFlakeFactory.getInstance();
    }


    @Override
    public void create(BugCreateDto createDto) {
        DesignerBug designerBug = new DesignerBug();
        User user = userRepository.findOne(createDto.getUserId());
        BeanUtils.copyProperties(createDto, designerBug);
        designerBug.setId(snowFlakeFactory.nextId());
        if (designerBug.getBugLevel() == null || designerBug.getBugLevel().length() == 0) {
            designerBug.setBugLevel(BugStatus.USUALLY);
        }
        designerBug.setUserName(user.getName());
        designerBug.setBugStatus(BugStatus.UNASSIGNED);
        designerBug.setDesignerVersion(designerBug.getDesignerVersion().toUpperCase().trim());
        designerBugRepository.save(designerBug);
        BugLog bugLog = new BugLog();
        bugLog.setId(snowFlakeFactory.nextId());
        bugLog.setUserId(user.getId());
        bugLog.setUserName(user.getName());
        bugLog.setBugId(designerBug.getId());
        bugLog.setOperation("在时间 " + LocalDateTime.now().format(formatter) + " 由 " + user.getName() + " 提出BUG");
        bugLogRepository.save(bugLog);
    }

    @Override
    public List<DesignerBug> countByUNRESOLVED(BugType type) {
        return designerBugRepository.findAllByBugStatusInAndType(Arrays.asList(BugStatus.UNASSIGNED,BugStatus.UNRESOLVED), type);
    }

    @Override
    public List<DesignerBug> countMyJobByUNRESOLVED(String userId) {
        return designerBugRepository.findAllByBugStatusInAndReviewUserId(Arrays.asList(BugStatus.UNASSIGNED,BugStatus.UNRESOLVED), userId);
    }

    @Override
    public void assign(BugAssignDto assignDto) {
        User assigner = userRepository.findOne(assignDto.getUserId());
        User reviewer = userRepository.findOne(assignDto.getReviewUserId());
        DesignerBug designerBug = designerBugRepository.findOne(assignDto.getBugId());
        designerBug.setReviewUserId(reviewer.getId());
        designerBug.setReviewUserName(reviewer.getName());
        designerBug.setBugStatus(BugStatus.UNRESOLVED);
        designerBugRepository.save(designerBug);
        BugLog bugLog = new BugLog();
        bugLog.setId(snowFlakeFactory.nextId());
        bugLog.setBugId(designerBug.getId());
        bugLog.setUserId(assigner.getId());
        bugLog.setUserName(assigner.getName());
        bugLog.setOperation("在时间 " + LocalDateTime.now().format(formatter) + " 由 " + assigner.getName() + " 把此任务指派给 " + reviewer.getName());
        bugLogRepository.save(bugLog);
    }

    @Override
    public void reAssign(BugAssignDto assignDto) {
        User assigner = userRepository.findOne(assignDto.getUserId());
        User reviewer = userRepository.findOne(assignDto.getReviewUserId());
        DesignerBug designerBug = designerBugRepository.findOne(assignDto.getBugId());
        designerBug.setReviewUserId(reviewer.getId());
        designerBug.setReviewUserName(reviewer.getName());
        designerBug.setBugStatus(BugStatus.UNRESOLVED);
        designerBugRepository.save(designerBug);
        BugLog bugLog = new BugLog();
        bugLog.setId(snowFlakeFactory.nextId());
        bugLog.setBugId(designerBug.getId());
        bugLog.setUserId(assigner.getId());
        bugLog.setUserName(assigner.getName());
        bugLog.setOperation("在时间 " + LocalDateTime.now().format(formatter) + " 由 " + assigner.getName() + " 把此任务重新指派给 " + reviewer.getName());
        bugLogRepository.save(bugLog);
    }

    @Override
    public void batchAssign(BugAssignDto assignDto) {
        User assigner = userRepository.findOne(assignDto.getUserId());
        User reviewer = userRepository.findOne(assignDto.getReviewUserId());
        assignDto.getBugIds().forEach(bugId->{
            DesignerBug designerBug = designerBugRepository.findOne(assignDto.getBugId());
            designerBug.setReviewUserId(reviewer.getId());
            designerBug.setReviewUserName(reviewer.getName());
            designerBug.setBugStatus(BugStatus.UNRESOLVED);
            designerBugRepository.save(designerBug);
            BugLog bugLog = new BugLog();
            bugLog.setId(snowFlakeFactory.nextId());
            bugLog.setBugId(designerBug.getId());
            bugLog.setUserId(assigner.getId());
            bugLog.setUserName(assigner.getName());
            bugLog.setOperation("在时间 " + LocalDateTime.now().format(formatter) + " 由 " + assigner.getName() + " 把此任务重新指派给 " + reviewer.getName());
            bugLogRepository.save(bugLog);
        });
    }

    @Override
    public List<DesignerBug> countByWeekFinished(BugType type) {
        LocalDate now = LocalDate.now();
        // 所在周开始时间
        LocalDate beginDayOfWeek = now.with(DayOfWeek.MONDAY);
        // 所在周结束时间
        LocalDate endDayOfWeek = now.with(DayOfWeek.SUNDAY);
        Specification<DesignerBug> specification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Predicate timeStart = criteriaBuilder.greaterThanOrEqualTo(root.get("reviewTime"), beginDayOfWeek.atStartOfDay());
            predicates.add(timeStart);
            Predicate timeEnd = criteriaBuilder.lessThanOrEqualTo(root.get("reviewTime"), endDayOfWeek.atTime(23, 59, 59));
            predicates.add(timeEnd);
            Predicate designer = criteriaBuilder.equal(root.get("type"), type);
            predicates.add(designer);
            Expression<String> exp = root.<String>get("bugStatus");
            List<String> bugStatusList = new ArrayList<>();
            bugStatusList.add("开发已处理");
            bugStatusList.add("开发已发布");
            predicates.add(exp.in(bugStatusList));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
        return designerBugRepository.findAll(specification);
    }


    @Override
    public List<DesignerBug> countMyJobByWeekFinished(String userId) {
        LocalDate now = LocalDate.now();
        // 所在周开始时间
        LocalDate beginDayOfWeek = now.with(DayOfWeek.MONDAY);
        // 所在周结束时间
        LocalDate endDayOfWeek = now.with(DayOfWeek.SUNDAY);
        Specification<DesignerBug> specification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Predicate timeStart = criteriaBuilder.greaterThanOrEqualTo(root.get("reviewTime"), beginDayOfWeek.atStartOfDay());
            predicates.add(timeStart);
            Predicate timeEnd = criteriaBuilder.lessThanOrEqualTo(root.get("reviewTime"), endDayOfWeek.atTime(23, 59, 59));
            predicates.add(timeEnd);
            Predicate myJob = criteriaBuilder.equal(root.get("reviewUserId"), userId);
            predicates.add(myJob);
            Expression<String> exp = root.<String>get("bugStatus");
            List<String> bugStatusList = new ArrayList<>();
            bugStatusList.add("开发已处理");
            bugStatusList.add("开发已发布");
            predicates.add(exp.in(bugStatusList));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
        return designerBugRepository.findAll(specification);
    }

    @Override
    public PageResult<DesignerBug> query(BugQueryParams params, BugType type) {
        Specification<DesignerBug> specification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params.getStartDate() != null) {
                Predicate timeStart = criteriaBuilder.greaterThanOrEqualTo(root.get("reviewTime"), params.getStartDate().atStartOfDay());
                predicates.add(timeStart);
            }
            if (params.getEndDate() != null) {
                Predicate timeEnd = criteriaBuilder.lessThanOrEqualTo(root.get("reviewTime"), params.getEndDate().atTime(23, 59, 59));
                predicates.add(timeEnd);
            }
            if (params.getVersion() != null && params.getVersion().length() > 0) {
                Predicate designerVersion = criteriaBuilder.equal(root.get("designerVersion"), params.getVersion());
                predicates.add(designerVersion);
            }
            if (params.getTitle() != null && params.getTitle().length() > 0) {
                Predicate designerVersion = criteriaBuilder.like(root.get("title"), params.getTitle() + "%");
                predicates.add(designerVersion);
            }
            if (params.getUserId() != null) {
                Predicate designerVersion = criteriaBuilder.equal(root.get("reviewUserId"), params.getUserId());
                predicates.add(designerVersion);
            }
            if (params.getBugStatus() != null && params.getBugStatus().length() > 0) {
                Predicate designerVersion = criteriaBuilder.equal(root.get("bugStatus"), params.getBugStatus());
                predicates.add(designerVersion);
            }
            if (params.getPublishVersion() != null && params.getPublishVersion().length() > 0) {
                Predicate designerVersion = criteriaBuilder.equal(root.get("publishVersion"), params.getPublishVersion());
                predicates.add(designerVersion);
            }
            if (params.getFixStatus() != null && params.getFixStatus().length() > 0) {
                Predicate designerVersion = criteriaBuilder.equal(root.get("fixStatus"), params.getFixStatus());
                predicates.add(designerVersion);
            }
            Predicate designer = criteriaBuilder.equal(root.get("type"), type);
            predicates.add(designer);
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest(params.getPage() - 1, params.getPageSize(), new Sort(order));
        Page<DesignerBug> designerBugPage = designerBugRepository.findAll(specification, pageable);
        return PageResult.of(designerBugPage.getContent(), params.getPage(), params.getPageSize(), designerBugPage.getTotalElements());
    }

    @Override
    public void solved(BugSolvedDto bugSolvedDto) {
        DesignerBug designerBug = designerBugRepository.findOne(bugSolvedDto.getBugId());
        User reviewer = userRepository.findOne(bugSolvedDto.getReviewUserId());
        designerBug.setReviewRemark(bugSolvedDto.getReviewRemark());
        designerBug.setBugStatus(BugStatus.HANDLED);
        designerBug.setReviewTime(LocalDateTime.now());
        designerBugRepository.save(designerBug);
        BugLog bugLog = new BugLog();
        bugLog.setId(snowFlakeFactory.nextId());
        bugLog.setUserId(reviewer.getId());
        bugLog.setUserName(reviewer.getName());
        bugLog.setBugId(designerBug.getId());
        bugLog.setOperation("在时间 " + LocalDateTime.now().format(formatter) + " 由 " + reviewer.getName() + " 完成BUG的修复");
        bugLogRepository.save(bugLog);
    }

    @Override
    public void finished(BugFinishedDto finishedDto) {
        DesignerBug designerBug = designerBugRepository.findOne(finishedDto.getBugId());
        User reviewer = userRepository.findOne(finishedDto.getReviewUserId());
        designerBug.setBugStatus(BugStatus.PUBLISHED);
        designerBug.setFixStatus(BugStatus.TESTED); // 已修复
        designerBugRepository.save(designerBug);
        BugLog bugLog = new BugLog();
        bugLog.setId(snowFlakeFactory.nextId());
        bugLog.setUserId(reviewer.getId());
        bugLog.setBugId(designerBug.getId());
        bugLog.setUserName(reviewer.getName());
        bugLog.setOperation("在时间 " + LocalDateTime.now().format(formatter) + " 由 " + reviewer.getName() + " 完成此BUG的功能修复验证");
        bugLogRepository.save(bugLog);
    }

    @Override
    public void reject(BugRejectDto rejectDto) {
        DesignerBug designerBug = designerBugRepository.findOne(rejectDto.getBugId());
        User reviewer = userRepository.findOne(rejectDto.getUserId());
        designerBug.setBugStatus(BugStatus.UNRESOLVED);
        designerBug.setFixStatus(BugStatus.FAILED);
        designerBugRepository.save(designerBug);
        BugLog bugLog = new BugLog();
        bugLog.setId(snowFlakeFactory.nextId());
        bugLog.setUserId(reviewer.getId());
        bugLog.setBugId(designerBug.getId());
        bugLog.setUserName(reviewer.getName());
        bugLog.setOperation("在时间 " + LocalDateTime.now().format(formatter) + " 由 " + reviewer.getName() + " 回退状态到 开发待解决。 原因：" + rejectDto.getRemark());
        bugLogRepository.save(bugLog);
    }

    @Override
    public void cancel(BugCancelDto cancelDto) {
        DesignerBug designerBug = designerBugRepository.findOne(cancelDto.getBugId());
        User cancelUser = userRepository.findOne(cancelDto.getUserId());
        designerBug.setBugStatus(BugStatus.CLOSED);
        designerBugRepository.save(designerBug);
        BugLog bugLog = new BugLog();
        bugLog.setId(snowFlakeFactory.nextId());
        bugLog.setUserId(cancelUser.getId());
        bugLog.setUserName(cancelUser.getName());
        bugLog.setOperation("在时间 " + LocalDateTime.now().format(formatter) + " 由 " + cancelUser.getName() + " 关闭BUG任务");
        bugLogRepository.save(bugLog);
    }

    @Override
    public BugDetailDto findById(String id) {
        DesignerBug designerBug = designerBugRepository.findOne(id);
        BugDetailDto bugDetailDto = new BugDetailDto();
        BeanUtils.copyProperties(designerBug, bugDetailDto);
        List<BugLog> logs = bugLogRepository.findByBugIdOrderByCreateTimeDesc(id);
        bugDetailDto.setLogList(logs);
        return bugDetailDto;
    }

    @Override
    public void publish(BugPublishDto publishDto) {
        String version = publishDto.getPublishVersion().toUpperCase().trim();
        List<DesignerBug> designerBugs = designerBugRepository.findAll(publishDto.getBugIds());
        User user = userRepository.findOne(publishDto.getUserId());
        designerBugs.forEach(designerBug -> Assert.isTrue("开发已处理".equals(designerBug.getBugStatus()), "只能勾选状态为[开发已处理]的进行发布"));
        designerBugs.forEach(designerBug -> designerBug.setBugStatus(BugStatus.PUBLISHED));// 已发布
        designerBugRepository.save(designerBugs);
        designerBugs.forEach(designerBug -> {
            designerBug.setPublishVersion(version);
            BugLog bugLog = new BugLog();
            bugLog.setId(snowFlakeFactory.nextId());
            bugLog.setBugId(designerBug.getId());
            bugLog.setUserId(user.getId());
            bugLog.setUserName(user.getName());
            bugLog.setOperation("在时间 " + LocalDateTime.now().format(formatter) + " 由 " + user.getName() + " 发布更新到版本[ " + version + "]");
            bugLogRepository.save(bugLog);
        });
    }
}
