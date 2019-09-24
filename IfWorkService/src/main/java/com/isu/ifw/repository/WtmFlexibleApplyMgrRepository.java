package com.isu.ifw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.isu.ifw.entity.WtmFlexibleApplyMgr;

@Repository
public interface WtmFlexibleApplyMgrRepository extends JpaRepository<WtmFlexibleApplyMgr, Long> {
	
	@Query(value="SELECT * FROM WTM_FLEXIBLE_APPLY WHERE TENANT_ID = :tenantId AND ENTER_CD = :enterCd AND REPLACE(:sYmd, '-', '') BETWEEN USE_SYMD AND USE_EYMD", nativeQuery = true)
	public List<WtmFlexibleApplyMgr> findByTenantIdAndEnterCdAndSymd(@Param(value="tenantId")Long tenantId, @Param(value="enterCd")String enterCd, @Param(value="sYmd")String sYmd);
}
