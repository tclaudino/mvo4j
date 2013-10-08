/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cd.mvo.client.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "contact_emails")
public class ContactEmail {

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;

	@Basic(optional = false)
	@Column(name = "EMAIL")
	private String email;
	@Column(name = "DISABLE_ALERT")
	private Boolean disableAlert;
	@JoinColumn(name = "CONTACT", referencedColumnName = "ID")
	@ManyToOne(optional = false)
	private ClientContact clientContact;

	public ContactEmail() {
	}

	public ContactEmail(String email) {
		this();
		this.email = email;
	}

	public ContactEmail(ClientContact clientContact) {
		this("");
		this.clientContact = clientContact;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getDisableAlert() {
		return disableAlert;
	}

	public void setDisableAlert(Boolean disableAlert) {
		this.disableAlert = disableAlert;
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
		if (!(object instanceof ContactEmail)) {
			return false;
		}
		ContactEmail other = (ContactEmail) object;
		if ((this.getId() == null && other.getId() != null)
				|| (this.getId() != null && !this.getId().equals(other.getId()))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return getId() + " - " + " - " + getEmail();
	}
}
