package com.isu.ifw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isu.ifw.entity.WtmOtCanAppl;

@Repository
public interface WtmOtCanApplRepository extends JpaRepository<WtmOtCanAppl, Long> {
	public WtmOtCanAppl findByApplId(Long applId);
}
