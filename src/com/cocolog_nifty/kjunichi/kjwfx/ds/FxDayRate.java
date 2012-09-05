package com.cocolog_nifty.kjunichi.kjwfx.ds;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.users.User;

/**
 * 日単位の為替レートを記録する
 * @author junichi
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FxDayRate {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String dateKey;

	@Persistent
	private String date;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Persistent
	private String currency;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Persistent
	private Float minTts;

	@Persistent
	private Float minTtb;

	@Persistent
	private Float maxTts;

	@Persistent
	private Float maxTtb;

	
	/**
	 * 
	 * @param date
	 * @param currency
	 * @param minTtb
	 * @param maxTtb
	 * @param minTts
	 * @param maxTts
	 */
	public FxDayRate(String date, String currency, Float minTtb,
			Float maxTtb,Float minTts,Float maxTts) {

		this.setDateKey(date,currency);

		this.setDate(date);
		
		// 購入通貨種類
		this.setCurrency(currency);

		// 取引レート
		this.setMinTtb(minTtb);
		this.setMaxTtb(maxTtb);

		this.setMinTts(minTts);
		this.setMaxTts(maxTts);

	}

	public String getDateKey() {
		return dateKey;
	}

	public void setDateKey(String date,String currency) {
		this.dateKey = date + currency;
	}

	public Float getMinTts() {
		return minTts;
	}

	public void setMinTts(Float minTts) {
		this.minTts = minTts;
	}

	public Float getMinTtb() {
		return minTtb;
	}

	public void setMinTtb(Float minTtb) {
		this.minTtb = minTtb;
	}

	public Float getMaxTts() {
		return maxTts;
	}

	public void setMaxTts(Float maxTts) {
		this.maxTts = maxTts;
	}

	public Float getMaxTtb() {
		return maxTtb;
	}

	public void setMaxTtb(Float maxTtb) {
		this.maxTtb = maxTtb;
	}

	
}
