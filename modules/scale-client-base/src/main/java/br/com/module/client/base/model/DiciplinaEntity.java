package br.com.module.client.base.model;

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
@Table(name = "diciplinas", catalog = "app_escola", schema = "", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME" }) })
public class DiciplinaEntity {

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;

	@Basic(optional = false)
	@Column(name = "DATE_INS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateIns;
	@Basic(optional = false)
	@Column(name = "NAME")
	@NotNull(message = "{err.name.length}")
	private String name;

	public DiciplinaEntity() {
		dateIns = new Date();
	}

	public DiciplinaEntity(String name) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DiciplinaEntity other = (DiciplinaEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Diciplina [id=" + id + ", name=" + name + "]";
	}

}
