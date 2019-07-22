package com.isu.ifw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isu.ifw.entity.WtmApplLine;

@Repository
public interface WtmApplLineRepository extends JpaRepository<WtmApplLine, Long> {
	public List<WtmApplLine> findByApplIdOrderByApprSeqAsc(Long applId);
	
	public WtmApplLine findByApplIdAndApprSeq(Long applId, int apprSeq);
}
