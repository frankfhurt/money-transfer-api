package com.moneytransfer.api.clients.create;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.moneytransfer.api.RestRequest;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
public class ClientCreateRequest extends RestRequest {

	@JsonProperty(required = true)
	@NotNull(message = "Field name is required")
	@Size(min = 1, max = 100)
	private String name;

	@JsonProperty(required = true)
	@NotNull(message = "Field document is required")
	@Size(min = 1, max = 50)
	private String document;
	
	@JsonProperty
	@Pattern(regexp = "^[+]?\\d+([.](\\d{0,2}))?$", message = "initialDeposit must be a valid positive number. Max of two decimals")
	private String initialDeposit;

}
