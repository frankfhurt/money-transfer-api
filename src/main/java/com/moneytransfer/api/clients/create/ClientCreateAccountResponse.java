package com.moneytransfer.api.clients.create;

import java.math.BigDecimal;

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
public class ClientCreateAccountResponse {

	private BigDecimal balance;

}
