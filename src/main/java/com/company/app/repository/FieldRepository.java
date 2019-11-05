package com.company.app.repository;

import java.util.List;

public interface FieldRepository {

    List<Integer> getAllFields();

    void addFields(List<Integer> list);

    void deleteFields(List<Integer> list);
}