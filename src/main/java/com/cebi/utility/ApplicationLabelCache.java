package com.cebi.utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cebi.entity.ApplicationLabel;
import com.cebi.entity.TableMetaData;

public class ApplicationLabelCache {

	private static Map<String, List<ApplicationLabel>> labelInstance = null;
	private static Map<String, List<TableMetaData>> viewInstance = null;
	private static Map<String,List<String>> bankDbDetailsInstance=null;

	private ApplicationLabelCache() {
	}

	public static Map<String, List<ApplicationLabel>> getLabelInstance() {
		if (labelInstance == null)
			labelInstance = new HashMap<String, List<ApplicationLabel>>();
		return labelInstance;
	}

	public static Map<String, List<TableMetaData>> getViewsInstance() {
		if (viewInstance == null)
			viewInstance = new HashMap<String, List<TableMetaData>>();
		return viewInstance;
	}
	public static Map<String, List<String>> getBankDbDetailsInstance() {
		if (bankDbDetailsInstance == null)
			bankDbDetailsInstance = new HashMap<String, List<String>>();
		return bankDbDetailsInstance;
	}
}
