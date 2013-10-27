package br.com.cd.mvo.client.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "client_contracts", uniqueConstraints = { @UniqueConstraint(columnNames = { "CONTRACT" }) })
public class ClientContract {

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "ACTIVE")
	private Boolean active;
	@Column(name = "CONTRACT")
	private Integer contract;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_INS")
	private Date dateIns;
	@Column(name = "VALUE")
	private double value;
	// bi-directional many-to-one association to ClientContact
	@OneToMany(mappedBy = "clientContract")
	private List<ClientContact> clientContactList;
	// bi-directional many-to-one association to Client
	@ManyToOne
	@JoinColumn(name = "CLIENT")
	private Client client;
	// bi-directional many-to-one association to ContractType
	@ManyToOne
	@JoinColumn(name = "TYPE")
	private ContractType contractType;

	public ClientContract() {
		dateIns = new Date();
	}

	public ClientContract(Client client) {
		this();
		this.client = client;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Integer getContract() {
		return this.contract;
	}

	public void setContract(Integer contract) {
		this.contract = contract;
	}

	public Date getDateIns() {
		return this.dateIns;
	}

	public void setDateIns(Date dateIns) {
		this.dateIns = dateIns;
	}

	public double getValue() {
		return this.value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public List<ClientContact> getClientContactList() {
		if (clientContactList == null) {
			clientContactList = new ArrayList<ClientContact>();
		}
		return clientContactList;
	}

	public void setClientContactList(List<ClientContact> clientContactList) {
		if (clientContactList != null) {
			this.clientContactList = clientContactList;
		}
	}

	public Client getClient() {
		return this.client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public ContractType getContractType() {
		return this.contractType;
	}

	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
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
		if (!(object instanceof ClientContract)) {
			return false;
		}
		ClientContract other = (ClientContract) object;
		if ((this.getId() == null && other.getId() != null)
				|| (this.getId() != null && !this.getId().equals(other.getId()))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return getId() + " - " + getContract() + " - " + getClient();
	}
}
