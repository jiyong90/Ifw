package com.isu.ifw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isu.ifw.entity.WtmTimeCdMgr;

@Repository
public interface WtmTimeCdMgrRepository extends JpaRepository<WtmTimeCdMgr, Long> {
	
	public List<WtmTimeCdMgr> findByTenantIdAndEnterCd(Long tenantId, String enterCd);
	
}
