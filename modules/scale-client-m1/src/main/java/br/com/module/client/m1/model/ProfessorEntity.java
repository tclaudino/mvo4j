package br.com.module.client.m1.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import br.com.module.client.base.model.DiciplinaEntity;

@Entity
@Table(name = "professores", catalog = "app_escola", schema = "", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME" }) })
public class ProfessorEntity {

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

	@ManyToOne(targetEntity = DiciplinaEntity.class)
	@JoinColumn(name = "DICIPLINA_ID", referencedColumnName = "ID")
	private DiciplinaEntity diciplina;

	public ProfessorEntity() {
		dateIns = new Date();
	}

	public ProfessorEntity(String name, DiciplinaEntity diciplina) {
		this();
		this.name = name;
		this.diciplina = diciplina;
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

	public DiciplinaEntity getDiciplina() {
		return diciplina;
	}

	public void setDiciplina(DiciplinaEntity diciplina) {
		this.diciplina = diciplina;
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
		ProfessorEntity other = (ProfessorEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Professor [id=" + id + ", name=" + name + "]";
	}

}
