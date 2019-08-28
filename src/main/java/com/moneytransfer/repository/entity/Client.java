package com.moneytransfer.repository.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Franklyn
 * @since 08/2019
 *
 */
@Getter
@Setter
@Entity(name = "client")
@Table(name = "client")
public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "document")
	private String document;

	@OneToOne(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Account account;

	public void setAccount(Account account) {
		if (account == null) {
			if (this.account != null) {
				this.account.setClient(null);
			}
		} else {
			account.setClient(this);
		}
		this.account = account;
	}

}