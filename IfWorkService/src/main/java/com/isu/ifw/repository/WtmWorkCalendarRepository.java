package com.isu.ifw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isu.ifw.entity.WtmWorkCalendar;

@Repository
public interface WtmWorkCalendarRepository extends JpaRepository<WtmWorkCalendar, Long> {
	public WtmWorkCalendar findByTenantIdAndEnterCdAndSabunAndYmd(Long tenantId, String enterCd, String sabun, String ymd);
}
