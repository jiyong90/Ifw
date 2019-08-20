package com.isu.ifw.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.isu.ifw.entity.WtmWorkDayResult;

@Repository
public interface WtmWorkDayResultRepository extends JpaRepository<WtmWorkDayResult, Long> {
	@Query("SELECT D FROM WtmWorkDayResult D JOIN WtmWorkCalendar C ON D.tenantId = C.tenantId AND D.enterCd = C.enterCd AND D.ymd = C.ymd AND D.sabun = C.sabun WHERE C.tenantId = ?1 AND C.enterCd = ?2 AND C.sabun = ?3 AND C.ymd = ?4")
	public List<WtmWorkDayResult> findByTenantIdAndEnterCdAndSabunAndYmd(Long tenantId, String enterCd, String sabun, String ymd);
	
	@Query("SELECT D FROM WtmWorkDayResult D JOIN WtmWorkCalendar C ON D.tenantId = C.tenantId AND D.enterCd = C.enterCd AND D.ymd = C.ymd AND D.sabun = C.sabun WHERE D.timeTypeCd = ?1 AND C.tenantId = ?2 AND C.enterCd = ?3 AND C.sabun = ?4 AND C.ymd = ?5")
	public WtmWorkDayResult findByTimeTypeCdAndTenantIdAndEnterCdAndSabunAndYmd(String timeTypeCd, Long tenantId, String enterCd, String sabun, String ymd);

	@Query("SELECT D FROM WtmWorkDayResult D JOIN WtmWorkCalendar C ON D.tenantId = C.tenantId AND D.enterCd = C.enterCd AND D.ymd = C.ymd AND D.sabun = C.sabun WHERE C.workCalendarId = ?1")
	public List<WtmWorkDayResult> findByWorkCalendarId(Long workCalendarId);
}
