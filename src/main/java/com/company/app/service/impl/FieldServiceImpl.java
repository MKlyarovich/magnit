package com.company.app.service.impl;

import com.company.app.repository.FieldRepository;
import com.company.app.service.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FieldServiceImpl implements FieldService {

    private final FieldRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Integer> getAllFields() {
        return repository.getAllFields();
    }

    @Override
    @Transactional
    public void addFields(List<Integer> list) {
        repository.addFields(list);
    }

    @Override
    @Transactional
    public void deleteFields(List<Integer> list) {
        repository.deleteFields(list);
    }
}