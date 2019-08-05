package com.isu.ifw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.isu.ifw.entity.WtmEmpHis;

@Repository
public interface WtmEmpHisRepository extends JpaRepository<WtmEmpHis, Long> {
	
	@Query(value="SELECT * FROM WTM_EMP_HIS WHERE TENANT_ID = :tenantId AND ENTER_CD = :enterCd AND (SABUN like %:sData% OR EMP_NM like %:sData%) AND IF(:sYmd='',date_format(now(),'%Y%m%d'),:sYmd) BETWEEN SYMD AND EYMD ", nativeQuery = true)
	public List<WtmEmpHis> findByTenantIdAndEnterCd(@Param(value="tenantId")Long tenantId, @Param(value="enterCd")String enterCd, @Param(value="sYmd")String sYmd, @Param(value="sData")String sData);
}
