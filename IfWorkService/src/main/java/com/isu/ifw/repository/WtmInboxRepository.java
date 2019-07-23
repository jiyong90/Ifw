package com.isu.ifw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isu.ifw.entity.WtmInbox;

@Repository
public interface WtmInboxRepository extends JpaRepository<WtmInbox, Long> {
	public int countByTenantIdAndEnterCdAndSabunAndCheckYn(Long tenantId, String enterCd, String sabun, String checkYn);
}
