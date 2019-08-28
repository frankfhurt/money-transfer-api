package com.moneytransfer.api.clients.detail;

import com.moneytransfer.api.RestRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
@Getter
@AllArgsConstructor
public class ClientDetailRequest extends RestRequest {
	
	private Long clientId;

}
