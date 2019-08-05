package com.isu.ifw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.isu.ifw.entity.WtmIfEmpMsg;

@Repository
public interface WtmIfEmpMsgRepository extends JpaRepository<WtmIfEmpMsg, Long> {
	
	@Query(value="SELECT IF_EMP_MSG_ID, TENANT_ID, ENTER_CD, SABUN, CHG_YMD, CHG_TYPE_CD, F_WTM_GET_CODE_NM(TENANT_ID, ENTER_CD, CHG_TYPE_CD, OLD_VALUE, date_format(now(),'%Y%m%d')) AS OLD_VALUE, F_WTM_GET_CODE_NM(TENANT_ID, ENTER_CD, CHG_TYPE_CD, NEW_VALUE, date_format(now(),'%Y%m%d')) AS NEW_VALUE, NOTE, UPDATE_DATE, UPDATE_ID FROM WTM_IF_EMP_MSG WHERE TENANT_ID = :tenantId AND ENTER_CD = :enterCd AND (SABUN like %:sData% ) AND IF(:sYmd='',date_format(now(),'%Y%m'),:sYmd) <= CHG_YMD ", nativeQuery = true)
	public List<WtmIfEmpMsg> findByTenantIdAndEnterCd(@Param(value="tenantId")Long tenantId, @Param(value="enterCd")String enterCd, @Param(value="sYmd")String sYmd, @Param(value="sData")String sData);
}
