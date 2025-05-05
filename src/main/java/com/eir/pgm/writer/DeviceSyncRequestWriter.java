package com.eir.pgm.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Repository
public class DeviceSyncRequestWriter extends Writter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${deviceSyncRequest.delete.filePath}")
    private String filePath;

    final String DATE = "<DATE>";
    String selectQuery = "select * from device_sync_request where request_date <'" + DATE + "' and status='SYNCED'";

    DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    DateTimeFormatter fileSuffixDateFormat = DateTimeFormatter.ofPattern("yyyyMMddHH");
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String fullFileHeader = "id,identity,device_type,imei,imsi,tac,instance_name,msisdn,no_of_retry,operation,request_date,status,sync_request_time,sync_response_time,failure_reason,priority,response_status";

    public void writeFullData(LocalDateTime endDate) {
        String queryStartDate = simpleDateFormat.format(endDate);
        String query = selectQuery.replaceAll(DATE, queryStartDate);
        String filename = "PM_DEVICE_SYNC_DELETE_" + endDate.format(fileSuffixDateFormat) + ".csv";
        String filepath = filePath + "/" + filename;

        PrintWriter writer = null;
        try {
            createFile(filepath);

            writer = new PrintWriter(filepath);
            writer.println(fullFileHeader);
            PrintWriter finalWriter = writer;
            jdbcTemplate.query(query, new RowCallbackHandler() {
                public void processRow(ResultSet resultSet) throws SQLException {
                    String id = nullToBlank(resultSet.getString("id"));
                    String identity = nullToBlank(resultSet.getString("identity"));
                    String device_type = nullToBlank(resultSet.getString("device_type"));
                    String imei = nullToBlank(resultSet.getString("imei"));
                    String imsi = nullToBlank(resultSet.getString("imsi"));
                    String tac = nullToBlank(resultSet.getString("tac"));
                    String instance_name = nullToBlank(resultSet.getString("instance_name"));
                    String msisdn = nullToBlank(resultSet.getString("msisdn"));
                    String no_of_retry = nullToBlank(resultSet.getString("no_of_retry"));
                    String operation = nullToBlank(resultSet.getString("operation"));
                    String request_date = nullToBlank(resultSet.getString("request_date"));
                    String status = nullToBlank(resultSet.getString("status"));
                    String sync_request_time = nullToBlank(resultSet.getString("sync_request_time"));
                    String sync_response_time = nullToBlank(resultSet.getString("sync_response_time"));
                    String failure_reason = nullToBlank(resultSet.getString("failure_reason"));
                    String priority = nullToBlank(resultSet.getString("priority"));
                    String response_status = nullToBlank(resultSet.getString("response_status"));
                    finalWriter.println(id + "," + identity + "," + device_type + "," + imei + "," + imsi + "," + tac + "," + instance_name + "," + msisdn + "," + no_of_retry
                            + "," + operation + "," + request_date + "," + status + "," + sync_request_time + "," + sync_response_time + "," + failure_reason + "," + priority + "," + response_status);
                }
            });
            log.info("File is written for Filename:{} Query:{}", filename, query);
        } catch (Exception e) {
            log.error("Error While creating file Allow tac Error:{}", e.getMessage(), e);
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    private String nullToBlank(String value) {
        return value == null ? "" : value;
    }
}
