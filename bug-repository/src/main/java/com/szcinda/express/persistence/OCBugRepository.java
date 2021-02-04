package com.szcinda.express.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OCBugRepository extends JpaRepository<OcBug, String>, JpaSpecificationExecutor<OcBug> {
}
