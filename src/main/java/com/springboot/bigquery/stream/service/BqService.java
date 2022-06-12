/**
 * 
 */
package com.springboot.bigquery.stream.service;

import com.google.cloud.bigquery.storage.v1.AppendRowsResponse;
import com.springboot.bigquery.stream.dto.DownStreamRequest;

/**
 * @author Shaikh Ahmed Reza
 *
 */
public interface BqService {

	AppendRowsResponse insertDownStreamRequestInBigQueryAuditTable(DownStreamRequest downStreamRequest);

	String getDownStreamRequestStatusByRequestId(String requestId);

}
