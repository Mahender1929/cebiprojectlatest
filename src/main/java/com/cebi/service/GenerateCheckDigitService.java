package com.cebi.service;

import java.util.List;

public interface GenerateCheckDigitService {

	public String populateParameters(List<String> checkDigitColumns,String table);
}
