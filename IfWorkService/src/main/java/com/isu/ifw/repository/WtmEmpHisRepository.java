package com.isu.ifw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.isu.ifw.entity.WtmEmpHis;

@Repository
public interface WtmEmpHisRepository extends JpaRepository<WtmEmpHis, Long> {
	
	@Query(value="SELECT * FROM WTM_EMP_HIS WHERE TENANT_ID = :tenantId AND ENTER_CD = :enterCd AND (SABUN like %:searchKeyword% OR EMP_NM like %:searchKeyword%) AND IF(:sYmd='',date_format(now(),'%Y%m%d'), REPLACE(:sYmd, '-', '')) BETWEEN SYMD AND EYMD ", nativeQuery = true)
	public List<WtmEmpHis> findByTenantIdAndEnterCd(@Param(value="tenantId")Long tenantId, @Param(value="enterCd")String enterCd, @Param(value="sYmd")String sYmd, @Param(value="searchKeyword")String searchKeyword);

    @Query(value="SELECT * FROM WTM_EMP_HIS WHERE TENANT_ID = :tenantId AND ENTER_CD = :enterCd AND SABUN = :sabun AND date_format(now(),'%Y%m%d') BETWEEN SYMD AND EYMD ", nativeQuery = true)
	public List<WtmEmpHis> findByTenantIdAndEnterCdAndSabun(@Param(value="tenantId")Long tenantId, @Param(value="enterCd")String enterCd, @Param(value="sabun")String sabun);

	public WtmEmpHis findByEmpHisId(Long empHisId);
}