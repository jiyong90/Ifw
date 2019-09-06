package com.isu.ifw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.isu.ifw.entity.WtmOtCanAppl;

@Repository
public interface WtmOtCanApplRepository extends JpaRepository<WtmOtCanAppl, Long> {
	public WtmOtCanAppl findByApplId(Long applId);
	
	@Query("SELECT r FROM WtmOtCanAppl r WHERE r.otApplId = (SELECT o.otApplId FROM WtmOtAppl o WHERE o.applId = ?1)")
	public WtmOtCanAppl findByOtApplId(Long applId);
}
