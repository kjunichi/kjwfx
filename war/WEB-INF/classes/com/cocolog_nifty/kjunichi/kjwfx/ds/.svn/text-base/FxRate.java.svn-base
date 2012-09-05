package com.cocolog_nifty.kjunichi.kjwfx.ds;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.users.User;

/**
 * 為替レートを記録する
 * @author junichi
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FxRate {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Persistent
	private Date date;

	@Persistent
	private String currency;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Persistent
	private Float tts;

	@Persistent
	private Float ttb;

	/**
	 * @param author
	 * @param date
	 * @param tts
	 * @param amount
	 */
	public FxRate(Date date, String currency, Float ttb, Float tts) {

		this.setDate(date);

		// 購入通貨種類
		this.setCurrency(currency);

		// 取引レート
		this.setTtb(ttb);

		this.setTts(tts);

	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Float getTts() {
		return this.tts;
	}

	public void setTts(Float tts) {
		this.tts = tts;
	}

	public Float getTtb() {
		return this.ttb;
	}

	public void setTtb(Float ttb) {
		this.ttb = ttb;
	}
}
