package br.com.cd.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "PCE_PARAMETRO_SISTEMA", uniqueConstraints = { @UniqueConstraint(columnNames = "CHAVE_PARAMETRO_SISTEMA", name = "UK_PRSIS_01") })
public class ParametroSistema {

	public static final String SQ_ID_PARAMETRO_SISTEMA = "PCE_SQ_PRSIS";

	@Id
	@SequenceGenerator(name = "sq.paramSis", sequenceName = ParametroSistema.SQ_ID_PARAMETRO_SISTEMA)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq.paramSis")
	@Column(name = "COD_PARAMETRO_SISTEMA", unique = true, nullable = false)
	private Integer id;

	@Column(name = "CHAVE_PARAMETRO_SISTEMA", unique = true, nullable = false, length = 200)
	private String chave;

	@Column(name = "VALOR_PARAMETRO_SISTEMA", nullable = false, length = 200)
	private String valor;

	public ParametroSistema() {
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the chave
	 */
	public String getChave() {
		return chave;
	}

	/**
	 * @param chave
	 *            the chave to set
	 */
	public void setChave(String chave) {
		this.chave = chave;
	}

	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * @param valor
	 *            the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParametroSistema other = (ParametroSistema) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ParametroSistema [id=" + id + ", chave=" + chave + ", valor="
				+ valor + "]";
	}

}
