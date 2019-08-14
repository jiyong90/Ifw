package com.isu.ifw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.isu.ifw.entity.WtmTimeCdMgr;

@Repository
public interface WtmTimeCdMgrRepository extends JpaRepository<WtmTimeCdMgr, Long> {
	@Query(value="SELECT * FROM WTM_TIME_CD_MGR WHERE TENANT_ID = :tenantId AND ENTER_CD = :enterCd AND (:sYmd BETWEEN SYMD AND IFNULL(EYMD, '99991231')) ORDER BY SYMD, TIME_CD", nativeQuery = true)
	public List<WtmTimeCdMgr> findByTenantIdAndEnterCd(@Param(value="tenantId")Long tenantId, @Param(value="enterCd")String enterCd, @Param(value="sYmd")String sYmd);
	
	
}
