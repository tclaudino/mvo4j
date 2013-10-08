/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cd.mvo.client.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "client_contacts")
public class ClientContact {

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;

	@Column(name = "DATE_INS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateIns;
	@Basic(optional = false)
	@Column(name = "NAME")
	private String name;
	@Column(name = "OBS")
	private String obs;
	@JoinColumn(name = "CONTRACT", referencedColumnName = "ID")
	@ManyToOne(optional = false)
	private ClientContract clientContract;
	@JoinColumn(name = "TYPE", referencedColumnName = "ID")
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private ContactType type;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "clientContact")
	private List<ContactPhone> phoneList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "clientContact")
	private List<ContactEmail> emailList;

	public ClientContact() {
		dateIns = new Date();
	}

	public ClientContact(ClientContract clientContract) {
		this();
		this.clientContract = clientContract;
	}

	public ClientContact(String name) {
		this();
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDateIns() {
		return dateIns;
	}

	public void setDateIns(Date dateIns) {
		this.dateIns = dateIns;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public ClientContract getClientContract() {
		return clientContract;
	}

	public void setClientContract(ClientContract clientContract) {
		this.clientContract = clientContract;
	}

	public ContactType getType() {
		return type;
	}

	public void setType(ContactType type) {
		this.type = type;
	}

	public List<ContactEmail> getEmailList() {
		if (emailList == null) {
			emailList = new ArrayList<ContactEmail>();
		}
		return emailList;
	}

	public void setEmailList(List<ContactEmail> emailList) {
		if (emailList != null) {
			this.emailList = emailList;
		}
	}

	public List<ContactPhone> getPhoneList() {
		if (phoneList == null) {
			phoneList = new ArrayList<ContactPhone>();
		}
		return phoneList;
	}

	public void setPhoneList(List<ContactPhone> phoneList) {
		if (phoneList != null) {
			this.phoneList = phoneList;
		}
		this.phoneList = phoneList;
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
		if (!(object instanceof ClientContact)) {
			return false;
		}
		ClientContact other = (ClientContact) object;
		if ((this.getId() == null && other.getId() != null)
				|| (this.getId() != null && !this.getId().equals(other.getId()))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return getId() + " - " + getClientContract() + " - " + getName();
	}
}
