package com.szcinda.express.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface BugLogRepository extends JpaRepository<BugLog, String>, JpaSpecificationExecutor<BugLog> {
    List<BugLog> findByBugIdOrderByCreateTimeDesc(String bugId);
}
