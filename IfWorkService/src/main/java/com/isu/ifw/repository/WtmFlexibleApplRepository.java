package com.isu.ifw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isu.ifw.entity.WtmFlexibleAppl;

@Repository
public interface WtmFlexibleApplRepository extends JpaRepository<WtmFlexibleAppl, Long> {
	public WtmFlexibleAppl findByApplId(Long applId);
}
