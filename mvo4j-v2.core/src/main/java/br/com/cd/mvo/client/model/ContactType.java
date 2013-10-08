/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cd.mvo.client.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "contact_types", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "TYPE" }),
		@UniqueConstraint(columnNames = { "ACRONYM" }) })
public class ContactType {

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;

	@Basic(optional = false)
	@Column(name = "DATE_INS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateIns;
	@Basic(optional = false)
	@Column(name = "TYPE")
	@NotNull
	private String type;
	@NotNull
	@Column(name = "ACRONYM")
	private String acronym;

	public ContactType() {
		this.dateIns = new Date();
	}

	public ContactType(String type) {
		this();
		this.type = type;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
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
		if (!(object instanceof ContactType)) {
			return false;
		}
		ContactType other = (ContactType) object;
		if ((this.getId() == null && other.getId() != null)
				|| (this.getId() != null && !this.getId().equals(other.getId()))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return getId() + " - " + getAcronym() + " - " + getType();
	}
}
