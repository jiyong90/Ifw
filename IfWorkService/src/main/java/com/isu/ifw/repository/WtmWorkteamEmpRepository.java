package com.isu.ifw.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.isu.ifw.entity.WtmWorkteamEmp;

@Repository
public interface WtmWorkteamEmpRepository extends JpaRepository<WtmWorkteamEmp, Long> {

	
	@Query(value="SELECT E.WORKTEAM_EMP_ID as workteamEmpID, E.WORKTEAM_CD as workteamCd, E.SABUN as sabun, E.SYMD as symd, E.EYMD as eymd, E.NOTE as note, C.ORG_CD as orgCd, C.CLASS_CD as classCd, C.EMP_NM as empNm "
			+ "FROM WTM_WORKTEAM_EMP E, WTM_EMP_HIS C "
			+ "WHERE E.TENANT_ID=:tenantId "
			+ "		AND E.SABUN = C.SABUN "
			+ "		AND IF(:sYmd='',date_format(now(),'%Y%m%d'),:sYmd) BETWEEN C.SYMD AND C.EYMD "
			+ "		AND E.ENTER_CD=:enterCd "
			+ "		AND IF(:sYmd='',date_format(now(),'%Y%m%d'),:sYmd) BETWEEN E.SYMD AND E.EYMD "
			+ "		AND (E.SABUN like %:sData% OR E.WORKTEAM_CD like %:sData% OR C.EMP_NM like %:sData%)", nativeQuery = true)
	public List<Map<String, Object>> findByTenantIdAndEnterCd(@Param(value="tenantId")Long tenantId, @Param(value="enterCd")String enterCd, @Param(value="sYmd")String sYmd, @Param(value="sData")String sData);
	
	public List<WtmWorkteamEmp> findByWorkteamMgrId(Long workteamMgrId);
}
