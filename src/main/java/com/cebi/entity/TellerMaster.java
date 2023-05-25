package com.cebi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * TellerMaster generated by hbm2java
 */
@Entity
@Table(name = "teller_master")
public class TellerMaster implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7576808287358423189L;

	@Id
	@Column(name = "TELLERID")
	private String tellerid;
	@Column(name = "PWD")
	private String pwd;
	@Column(name = "CAPABILITY")
	private Long capability;
	@Column(name = "ENABLED")
	private long enabled;
	@Column(name = "TELLERTYPE")
	private Long tellertype;
	@Column(name = "IP")
	private String ip;
	@Column(name = "SIGNONCOUNT")
	private Short signoncount;
	@Column(name = "TELLER_NM")
	private String tellerNm;
	@Column(name = "BRANCHID")
	private Integer branchid;
	@Column(name = "BANK_CODE")
	private String bankCode;
	@Column(name = "ccdp",nullable=true)
	private boolean ccdp;

	@Transient
	private String bankName;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getTellerid() {
		return tellerid;
	}

	public String getPwd() {
		return pwd;
	}

	public Long getCapability() {
		return capability;
	}

	public long getEnabled() {
		return enabled;
	}

	public Long getTellertype() {
		return tellertype;
	}

	public String getIp() {
		return ip;
	}

	public Short getSignoncount() {
		return signoncount;
	}

	public String getTellerNm() {
		return tellerNm;
	}

	public Integer getBranchid() {
		return branchid;
	}

	public String getBankName() {
		return bankName;
	}

	public void setTellerid(String tellerid) {
		this.tellerid = tellerid;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public void setCapability(Long capability) {
		this.capability = capability;
	}

	public void setEnabled(long enabled) {
		this.enabled = enabled;
	}

	public void setTellertype(Long tellertype) {
		this.tellertype = tellertype;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setSignoncount(Short signoncount) {
		this.signoncount = signoncount;
	}

	public void setTellerNm(String tellerNm) {
		this.tellerNm = tellerNm;
	}

	public void setBranchid(Integer branchid) {
		this.branchid = branchid;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public boolean isCcdp() {
		return ccdp;
	}

	public void setCcdp(boolean ccdp) {
		this.ccdp = ccdp;
	}


}
