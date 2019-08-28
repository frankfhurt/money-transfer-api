package com.moneytransfer.repository.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Entity(name = "account")
@Table(name = "account")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "balance")
	private BigDecimal balance;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_id")
	private Client client;

	public void addBalance(String value) {
		balance = balance.add(new BigDecimal(value));
	}

	public void subtractBalance(String value) {
		balance = balance.subtract(new BigDecimal(value));
	}

}