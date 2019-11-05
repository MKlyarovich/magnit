package com.company.app.service;

import com.company.app.model.Entries;

import java.nio.file.Path;
import java.util.List;

public interface TransformationService {

    Path objectToXml(Entries entries);

    Path xmlToXml(Path path);

    List<Integer> xmlToObject(Path path);
}