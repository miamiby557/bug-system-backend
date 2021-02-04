package com.szcinda.express.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ExecutorBugRepository extends JpaRepository<ExecutorBug, String>, JpaSpecificationExecutor<ExecutorBug> {
}
