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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "clients", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME" }) })
public class Client {

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Basic(optional = false)
	@Column(name = "DATE_INS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateIns;
	@Basic(optional = false)
	@Column(name = "NAME")
	@NotNull(message = "{err.name.length}")
	private String name;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "client", orphanRemoval = true)
	@Size(min = 1)
	@NotNull
	private List<ClientContract> clientContractList;

	public Client() {
		dateIns = new Date();
	}

	public Client(String name) {
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

	public List<ClientContract> getClientContractList() {
		if (clientContractList == null) {
			clientContractList = new ArrayList<ClientContract>();
		}
		return clientContractList;
	}

	public void setClientContractList(List<ClientContract> clientContractList) {
		if (clientContractList != null) {
			this.clientContractList = clientContractList;
		}
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
		Client other = (Client) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Client [id=" + id + ", name=" + name + "]";
	}

}
