/**
 * 
 */
package com.springboot.bigquery.stream.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.bigquery.storage.v1.AppendRowsResponse;
import com.springboot.bigquery.stream.dto.DownStreamRequest;
import com.springboot.bigquery.stream.service.BqService;

/**
 * @author Shaikh Ahmed Reza
 *
 */
@RestController
@RequestMapping("/api/v1/bigquery")
public class BqController {

	private final BqService bqService;

	/**
	 * @param bqService
	 */
	public BqController(BqService bqService) {
		this.bqService = bqService;
	}

	@GetMapping("/")
	public String welcome() {
		return "Welcome to BigQuery";
	}

	@PostMapping("/process-downstream-request")
	public AppendRowsResponse insertDownStreamRequestInBigQueryAuditTable(
			@RequestBody @Valid DownStreamRequest downStreamRequest) {

		AppendRowsResponse appendRowsResponse = bqService
				.insertDownStreamRequestInBigQueryAuditTable(downStreamRequest);

		return appendRowsResponse;
	}

	@GetMapping("/get-downstream-request/{requestId}")
	public String getDownStreamRequestStatusByRequestId(@PathVariable(name = "requestId") String requestId) {

		return bqService.getDownStreamRequestStatusByRequestId(requestId);
	}

}
