package com.cebi.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cebi.dao.LoginDao;
import com.cebi.entity.Banks;
import com.cebi.entity.TellerMaster;

@Service
public class LoginServiceImpl implements LoginService {
	private static final Logger logger = Logger.getLogger(LoginServiceImpl.class);

	@Autowired
	LoginDao loginDao;

	@Override
	public List<Object[]> validateLoginUser(TellerMaster tellerMaster) {
		logger.info("Inside validateLoginUser()");
	//	tellerMaster.setPwd(AES.getMD5EncryptedValue(tellerMaster.getPwd()));
		return loginDao.validateLoginUser(tellerMaster);
	}

	@Override
	public boolean runScript(String bankName) {
		logger.info("Inside validateLoginUser()");
		return loginDao.runScript(bankName);
	}
	
	@Override
	public List<Object[]> validateSuperLoginUser(TellerMaster tellerMaster) {
		//tellerMaster.setPwd(AES.getMD5EncryptedValue(tellerMaster.getPwd()));
		return loginDao.validateSuperLoginUser(tellerMaster);
	}

	@Override
	public List<String> checkbankcode(String database_url,TellerMaster master) {
		return loginDao.checkbankcode(database_url,master);
		// TODO Auto-generated method stub
		
	}

}
