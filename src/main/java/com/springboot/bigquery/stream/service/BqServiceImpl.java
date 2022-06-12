/**
 * 
 */
package com.springboot.bigquery.stream.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.JobId;
import com.google.cloud.bigquery.JobInfo;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;
import com.google.cloud.bigquery.storage.v1.AppendRowsResponse;
import com.google.cloud.bigquery.storage.v1.BigQueryWriteClient;
import com.google.cloud.bigquery.storage.v1.CreateWriteStreamRequest;
import com.google.cloud.bigquery.storage.v1.FinalizeWriteStreamRequest;
import com.google.cloud.bigquery.storage.v1.JsonStreamWriter;
import com.google.cloud.bigquery.storage.v1.TableName;
import com.google.cloud.bigquery.storage.v1.WriteStream;
import com.springboot.bigquery.stream.dto.DownStreamRequest;

/**
 * @author Shaikh Ahmed Reza
 *
 */
@Service
public class BqServiceImpl implements BqService {

	@Autowired
	ObjectMapper objectMapper;

	@Override
	public AppendRowsResponse insertDownStreamRequestInBigQueryAuditTable(DownStreamRequest downStreamRequest) {

		try (BigQueryWriteClient client = BigQueryWriteClient.create()) {
			String requestPayload = objectMapper.writeValueAsString(downStreamRequest);
			WriteStream stream = WriteStream.newBuilder().setType(WriteStream.Type.COMMITTED).build();
			TableName parentTable = TableName.of("q-gcp-5140-ford-gdia-21-08", "bq_access_provision_ds",
					"downstream_request_audit_tb");
			System.out.println("Table object created " + parentTable);
			CreateWriteStreamRequest createWriteStreamRequest = CreateWriteStreamRequest.newBuilder()
					.setParent(parentTable.toString()).setWriteStream(stream).build();
			System.out.println("Writestream request created " + createWriteStreamRequest);
			WriteStream writeStream = client.createWriteStream(createWriteStreamRequest);
			System.out.println("Writestream object created with client " + writeStream);
			try (JsonStreamWriter writer = JsonStreamWriter
					.newBuilder(writeStream.getName(), writeStream.getTableSchema()).build()) {
				JSONArray jsonArr = new JSONArray();
				JSONObject record = new JSONObject();
				record.put("request_id", downStreamRequest.getRequestId());
				record.put("source", downStreamRequest.getSource());
				record.put("project_type", downStreamRequest.getProjectType());
				record.put("environment", downStreamRequest.getEnvironment());
				record.put("downstream_application_eams_number",
						downStreamRequest.getDownStreamApplicationEamsNumber());
				record.put("downstream_service_account_name",
						downStreamRequest.getDownStreamApplicationServiceAccount());
				record.put("action", downStreamRequest.getAction());
				record.put("request_payload", requestPayload);
				record.put("request_status", "Success");
				record.put("error_code", "null");
				record.put("error_message", "null");
				record.put("request_approved_timestamp", downStreamRequest.getLastApprovedTimeStamp());
				record.put("request_received_timestamp", downStreamRequest.getLastApprovedTimeStamp());
				record.put("create_timestamp", getUtcTime());
				jsonArr.put(record);
				ApiFuture<AppendRowsResponse> future = writer.append(jsonArr);
				AppendRowsResponse response = future.get();
				FinalizeWriteStreamRequest finalizeWriteStreamRequest = FinalizeWriteStreamRequest.newBuilder()
						.setName(writeStream.getName()).build();
				client.finalizeWriteStream(finalizeWriteStreamRequest);

				return response;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to insert request in to enduser_request_tb :" + e.getMessage());
		}

	}

	public String getUtcTime() {
		Instant instant = Instant.now();
		ZonedDateTime zdt = instant.atZone(ZoneId.of("UTC"));
		LocalDateTime dateTime = zdt.toLocalDateTime();
		Timestamp timestamp = Timestamp.valueOf(dateTime);
		return timestamp.toString();
	}

	@Override
	public String getDownStreamRequestStatusByRequestId(String requestId) {

		String payload = null;
		TableResult resultSet = null;
		String bigQueryTableName = "q-gcp-5140-ford-gdia-21-08" + "." + "bq_access_provision_ds" + "."
				+ "downstream_request_audit_tb";

		String query = "SELECT request_status FROM `" + bigQueryTableName + "` " + "WHERE request_id=" + "'" + requestId
				+ "' " + "orderby DESC LIMIT 1";
		;

		System.out.println("Query:" + query);

		try {
			Job queryJob = getBigQueryJob(query);
			// Wait for the query to complete.
			queryJob = queryJob.waitFor();
			if (queryJob == null) {
				throw new RuntimeException("Big Query job no longer exists, unable to lookup latest end user request.");
			} else if (queryJob.getStatus().getError() != null) {
				throw new RuntimeException("Big Query job received an error, "
						+ "unable to do latest end user request lookup in Big query. "
						+ "Please try gain later or contact AMP team." + queryJob.getStatus().getError().toString());
			}
			resultSet = queryJob.getQueryResults();
			for (final FieldValueList row : resultSet.iterateAll()) {
				payload = row.get("request_status").getStringValue();

			}

		} catch (InterruptedException e) {
			throw new RuntimeException("Failed to retrieve payload for request ID:" + requestId + ":" + e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException("Failed to retrieve payload for request ID:" + requestId + ":" + e.getMessage());
		}
		return payload;
	}

	private Job getBigQueryJob(String query) {
		Job queryJob = null;
		try {
			// Impersonation token will be set only while running in local environment .
			// i.e. active profile will be local
			BigQuery bigQuery = BigQueryOptions.getDefaultInstance().getService();
			final QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).setUseLegacySql(false)
					.build();
			final JobId jobId = JobId.of(UUID.randomUUID().toString());
			queryJob = bigQuery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());
		} catch (Exception e) {
			throw new RuntimeException(
					"Error creating Big Query job." + e.getMessage() + ".Please contact AMP team or try again later");
		}
		return queryJob;
	}

}
