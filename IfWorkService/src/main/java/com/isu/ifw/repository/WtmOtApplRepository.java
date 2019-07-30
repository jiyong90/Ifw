package com.isu.ifw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isu.ifw.entity.WtmOtAppl;

@Repository
public interface WtmOtApplRepository extends JpaRepository<WtmOtAppl, Long> {
	public WtmOtAppl findByApplId(Long applId);
}
