package com.company.app.repository.impl;

import com.company.app.repository.FieldRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FieldRepositoryImpl implements FieldRepository {

    private static final String FIND_ALL_FIELDS = "SELECT T.FIELD FROM TEST T";
    private static final String ADD_FIELDS = "INSERT INTO TEST(FIELD) VALUES (?)";
    private static final String DELETE_FIELDS = "DELETE FROM TEST T WHERE T.FIELD = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Integer> getAllFields() {
        log.info("Receiving all fields");
        return jdbcTemplate.query(FIND_ALL_FIELDS, (rs, rowNum) -> rs.getInt("FIELD"));
    }

    @Override
    public void addFields(List<Integer> list) {
        log.info("Add fields");
        jdbcTemplate.batchUpdate(ADD_FIELDS,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, list.get(i));
                    }

                    @Override
                    public int getBatchSize() {
                        return list.size();
                    }
                });
    }

    @Override
    public void deleteFields(List<Integer> list) {
        log.info("Delete fields");
        jdbcTemplate.batchUpdate(DELETE_FIELDS,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, list.get(i));
                    }

                    @Override
                    public int getBatchSize() {
                        return list.size();
                    }
                });
    }
}