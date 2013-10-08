package br.com.cd.mvo.client.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "contract_types", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "TYPE" }),
		@UniqueConstraint(columnNames = { "ACRONYM" }) })
public class ContractType {

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;

	@Column(name = "ACRONYM")
	private String acronym;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_INS")
	private Date dateIns;
	@Column(name = "PERIODICITY")
	private Integer periodicity;
	@Column(name = "TYPE")
	private String type;

	public ContractType() {
	}

	public String getAcronym() {
		return this.acronym;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public Date getDateIns() {
		return this.dateIns;
	}

	public void setDateIns(Date dateIns) {
		this.dateIns = dateIns;
	}

	public Integer getPeriodicity() {
		return this.periodicity;
	}

	public void setPeriodicity(Integer periodicity) {
		this.periodicity = periodicity;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
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
		if (!(object instanceof ContractType)) {
			return false;
		}
		ContractType other = (ContractType) object;
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
