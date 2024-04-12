package com.example.backend1640.service.impl;

import com.example.backend1640.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.zip.*;

@Service
public class DatabaseServiceImpl implements DatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public byte[] exportAllTablesToCSV() throws IOException {
        List<String> tableNames = jdbcTemplate.queryForList(
                "SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname != 'pg_catalog' AND schemaname != 'information_schema'",
                String.class
        );
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (String tableName : tableNames) {
                if (!tableName.equals("flyway_schema_history")) {
                    byte[] csvData = exportTableToCSV(tableName);
                    ZipEntry zipEntry = new ZipEntry(tableName + ".csv");
                    zos.putNextEntry(zipEntry);
                    zos.write(csvData);
                    zos.closeEntry();
                }
            }
        }
        return baos.toByteArray();
    }

    private byte[] exportTableToCSV(String tableName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(baos, StandardCharsets.UTF_8));
        jdbcTemplate.query("SELECT * FROM " + tableName, new RowCallbackHandler() {
            boolean headerWritten = false;

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                if (!headerWritten) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        writer.print(metaData.getColumnName(i));
                        if (i != metaData.getColumnCount()) {
                            writer.print(",");
                        }
                    }
                    writer.println();
                    headerWritten = true;
                }

                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    writer.print(StringUtils.quoteIfString(rs.getObject(i)));
                    if (i != rs.getMetaData().getColumnCount()) {
                        writer.print(",");
                    }
                }
                writer.println();
            }
        });
        writer.close();
        return baos.toByteArray();
    }
}