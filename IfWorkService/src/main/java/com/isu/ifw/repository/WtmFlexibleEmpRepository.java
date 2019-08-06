package com.isu.ifw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.isu.ifw.entity.WtmFlexibleEmp;

@Repository
public interface WtmFlexibleEmpRepository extends JpaRepository<WtmFlexibleEmp, Long> {
	
	@Query("SELECT E FROM WtmFlexibleEmp E WHERE E.tenantId = ?1 AND E.enterCd = ?2 AND E.sabun = ?3 AND (?4 BETWEEN E.symd AND E.eymd OR F_WTM_TO_DATE(?4,'%Y%m%d') < F_WTM_TO_DATE(E.eymd,'%Y%m%d'))")
	public WtmFlexibleEmp findByTenantIdAndEnterCdAndSabunAndDate(Long tenantId, String enterCd, String sabun, String d);
	/**
	 * 특정일에 속한 근무제 정보를 가져오기 위함.
	 * @param tenantId
	 * @param enterCd
	 * @param sabun
	 * @param d
	 * @return
	 */
	@Query("SELECT E FROM WtmFlexibleEmp E WHERE E.tenantId = ?1 AND E.enterCd = ?2 AND E.sabun = ?3 AND ?4 BETWEEN E.symd AND E.eymd")
	public WtmFlexibleEmp findByTenantIdAndEnterCdAndSabunAndYmdBetween(Long tenantId, String enterCd, String sabun, String d);
}
