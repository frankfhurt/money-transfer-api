package com.moneytransfer.api.clients.detail;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
@Getter
@Setter
public class ClientDetailResponse {
	
	private Long id;

	private String name;

	private String document;

	private ClientDetailAccountResponse account;

}
