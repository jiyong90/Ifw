package com.isu.ifw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.isu.ifw.entity.WtmCode;

@Repository
public interface WtmCodeRepository extends JpaRepository<WtmCode, Long> {
	
	@Query(value="SELECT * FROM WTM_CODE WHERE TENANT_ID = :tenantId AND ENTER_CD = :enterCd AND date_format(now(),'%Y%m%d') BETWEEN SYMD AND EYMD AND GRP_CODE_CD = :grpCodeCd ORDER BY SEQ", nativeQuery = true)
	public List<WtmCode> findByTenantIdAndEnterCd(@Param(value="tenantId")Long tenantId, @Param(value="enterCd")String enterCd, @Param(value="grpCodeCd")String grpCodeCd);
	
	@Query(value="SELECT * FROM WTM_CODE WHERE TENANT_ID = :tenantId AND ENTER_CD = :enterCd "
			   + "AND REPLACE(:ymd, '-', '') BETWEEN SYMD AND EYMD "
			   + "AND GRP_CODE_CD = :grpCodeCd "
			   + "ORDER BY SEQ", nativeQuery = true)
	public List<WtmCode> findByTenantIdAndEnterCdAndGrpCodeCdAndYmd(@Param(value="tenantId")Long tenantId, @Param(value="enterCd")String enterCd, @Param(value="grpCodeCd")String grpCodeCd, @Param(value="ymd")String ymd);
}
