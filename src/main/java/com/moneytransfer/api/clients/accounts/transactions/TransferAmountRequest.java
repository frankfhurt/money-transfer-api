package com.moneytransfer.api.clients.accounts.transactions;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.moneytransfer.api.RestRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferAmountRequest extends RestRequest {

	@JsonIgnore
	private Long fromClientId;
	
	@NotNull(message = "amount cannot be null")
	@Pattern(regexp = "^[+]?\\d+([.](\\d{0,2}))?$", message = "amount must be a valid positive number. Max of two decimals")
	@JsonProperty(required = true)
	private String amount;

	@NotNull(message = "toClientId cannot be null")
	@Pattern(regexp = "^\\d+$", message = "toClientId must be a valid positive number")
	@JsonProperty(required = true)
	private String toClientId;

}