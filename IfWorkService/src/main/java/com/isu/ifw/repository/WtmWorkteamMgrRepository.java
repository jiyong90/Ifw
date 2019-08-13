package com.isu.ifw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isu.ifw.entity.WtmWorkteamMgr;

@Repository
public interface WtmWorkteamMgrRepository extends JpaRepository<WtmWorkteamMgr, Long> {
}
