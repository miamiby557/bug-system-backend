package com.szcinda.express.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DesignerBugRepository extends JpaRepository<DesignerBug, String>, JpaSpecificationExecutor<DesignerBug> {
    List<DesignerBug> findAllByBugStatusInAndType(List<String> bugStatusList, BugType type);

    List<DesignerBug> findAllByBugStatusInAndReviewUserId(List<String> bugStatusList, String userId);
}
