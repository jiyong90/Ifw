package com.isu.ifw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isu.ifw.entity.WtmApplCode;

@Repository
public interface WtmApplCodeRepository extends JpaRepository<WtmApplCode, Long> {
	public WtmApplCode findByTenantIdAndEnterCdAndApplCd(Long tenantId, String enterCd, String applCd);
}
