package com.cebi.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.cebi.dao.ApplicationLabelDao;
import com.cebi.entity.ApplicationLabel;

@Repository
public class ApplicationLabelServiceImpl implements ApplicationLabelService {

	@Autowired
	ApplicationLabelDao applicationLabelDao;

	public List<ApplicationLabel> retrieveAllLabels() {

		return applicationLabelDao.retrieveAllLabels();
	}
}