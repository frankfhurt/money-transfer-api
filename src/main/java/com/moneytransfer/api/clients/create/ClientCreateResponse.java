package com.moneytransfer.api.clients.create;

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
public class ClientCreateResponse {

	private Long id;

	private String name;

	private ClientCreateAccountResponse account;

}
