package com.company.app.service;

import java.util.List;

public interface FieldService {

    List<Integer> getAllFields();

    void addFields(List<Integer> list);

    void deleteFields(List<Integer> list);
}