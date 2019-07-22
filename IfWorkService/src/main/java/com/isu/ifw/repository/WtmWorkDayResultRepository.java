package com.isu.ifw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isu.ifw.entity.WtmWorkDayResult;

@Repository
public interface WtmWorkDayResultRepository extends JpaRepository<WtmWorkDayResult, Long> {
	
}
