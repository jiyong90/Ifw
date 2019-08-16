package com.isu.ifw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.isu.ifw.entity.WtmWorkteamMgr;

@Repository
public interface WtmWorkteamMgrRepository extends JpaRepository<WtmWorkteamMgr, Long> {
	@Query(value="SELECT * FROM WTM_WORKTEAM_MGR WHERE TENANT_ID = :tenantId AND ENTER_CD = :enterCd AND IF(:sYmd='',date_format(now(),'%Y%m%d'),:sYmd) BETWEEN SYMD AND EYMD ", nativeQuery = true)
	public List<WtmWorkteamMgr> findByTenantIdAndEnterCd(@Param(value="tenantId")Long tenantId, @Param(value="enterCd")String enterCd, @Param(value="sYmd")String sYmd);
}
