package com.company.app.service.impl;

import com.company.app.model.Entries;
import com.company.app.model.Entry;
import com.company.app.service.CalculationService;
import com.company.app.service.FieldService;
import com.company.app.service.TransformationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculationServiceImpl implements CalculationService {

    private final FieldService fieldService;
    private final TransformationService transformationService;

    @Transactional
    @Override
    public Long calculate(int value) {
        List<Integer> generateList = generateList(value);
        List<Integer> allFields = fieldService.getAllFields();

        Map<Boolean, List<Integer>> collect = generateList.parallelStream()
                .collect(Collectors.partitioningBy(allFields::contains));

        fieldService.deleteFields(collect.get(true));
        fieldService.addFields(collect.get(false));

        allFields = fieldService.getAllFields();
        if (Objects.isNull(allFields) || allFields.isEmpty()) {
            return 0L;
        }

        Path xmlPath = transformationService.objectToXml(convertToEntries(allFields));
        Path xmlPathAfterXsltTransform = transformationService.xmlToXml(xmlPath);
        List<Integer> integersFromXml = transformationService.xmlToObject(xmlPathAfterXsltTransform);

        return integersFromXml.parallelStream()
                .filter(Objects::nonNull)
                .mapToLong(Long::valueOf)
                .sum();
    }

    private List<Integer> generateList(int value) {
        return IntStream.rangeClosed(1, value)
                .parallel()
                .boxed()
                .collect(Collectors.toList());
    }

    private Entries convertToEntries(List<Integer> list) {
        final List<Entry> entryList = list.parallelStream()
                .map(v -> Entry.builder()
                        .field(v)
                        .build())
                .collect(Collectors.toList());

        return Entries.builder()
                .entry(entryList)
                .build();
    }
}