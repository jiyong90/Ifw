package com.isu.ifw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isu.ifw.entity.WtmFlexibleStdMgr;

@Repository
public interface WtmFlexibleStdMgrRepository extends JpaRepository<WtmFlexibleStdMgr, Long> {

}
