package com.cebi.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cebi.entity.Banks;
import com.cebi.entity.TellerMaster;

public interface LoginDao {
	
	public List<Object[]> validateLoginUser(TellerMaster tellerMaster);

	public boolean runScript(String bankName) ;

	public List<Object[]> validateSuperLoginUser(TellerMaster tellerMaster);

	public List<String> checkbankcode(String dburl,TellerMaster master);
}
