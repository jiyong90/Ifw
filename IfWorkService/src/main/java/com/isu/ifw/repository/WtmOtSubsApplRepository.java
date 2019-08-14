package com.isu.ifw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isu.ifw.entity.WtmOtSubsAppl;

@Repository
public interface WtmOtSubsApplRepository extends JpaRepository<WtmOtSubsAppl, Long> {
	public List<WtmOtSubsAppl> findByOtApplId(Long otApplId);
}
