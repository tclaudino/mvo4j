/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springview.client.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "contact_phones", catalog = "scheduler_visit", schema = "")
public class ContactPhone {

	public enum PhoneType {

		HOME, CELLULAR, BUSINESS;

		@Override
		public String toString() {
			switch (this) {
			case HOME:
				return "Home";
			case CELLULAR:
				return "Cellular";
			case BUSINESS:
				return "Business";
			default:
				return "Home";
			}
		}
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;

	@Basic(optional = false)
	@Column(name = "PHONE")
	private String phone;
	@Basic(optional = false)
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "TYPE")
	private PhoneType type;
	@Column(name = "OBS")
	private String obs;
	@JoinColumn(name = "CONTACT", referencedColumnName = "ID")
	@ManyToOne(optional = false)
	private ClientContact clientContact;

	public ContactPhone() {
		type = PhoneType.HOME;
	}

	public ContactPhone(String phone, PhoneType type) {
		this.phone = phone;
		this.type = type;
	}

	public ContactPhone(ClientContact clientContact) {
		this("", PhoneType.HOME);
		this.clientContact = clientContact;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public PhoneType getType() {
		return type;
	}

	public void setType(PhoneType type) {
		this.type = type;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public ClientContact getClientContact() {
		return clientContact;
	}

	public void setClientContact(ClientContact clientContact) {
		this.clientContact = clientContact;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (getId() != null ? getId().hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof ContactPhone)) {
			return false;
		}
		ContactPhone other = (ContactPhone) object;
		if ((this.getId() == null && other.getId() != null)
				|| (this.getId() != null && !this.getId().equals(other.getId()))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return getId() + " - " + " - " + getType() + " - " + getPhone();
	}
}
