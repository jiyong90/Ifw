package com.isu.ifw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isu.ifw.mapper.WtmFlexibleApplMapper;

@Service
public class WtmFlexibleApplServiceImpl implements WtmFlexibleApplService {

	@Autowired
	WtmFlexibleApplMapper flexApplMapper;
	
	
	
}
