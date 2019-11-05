package com.company.app.service.impl;

import com.company.app.model.Entries;
import com.company.app.service.TransformationService;
import com.company.app.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class TransformationServiceImpl implements TransformationService {

    @Override
    @Transactional
    public Path objectToXml(Entries entries) {
        try {
            File file = new File("1.xml");

            JAXBContext jaxbContext = JAXBContext.newInstance(Entries.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(entries, file);

            return file.toPath();
        } catch (JAXBException e) {
            String errorMessage = String.format("Could not transform entity: %s. %s", entries, e);
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    @Override
    @Transactional
    public Path xmlToXml(Path path) {
        try {
            File file = new File("2.xml");

            TransformerFactory factory = TransformerFactory.newInstance();
            Source xslt = new StreamSource(Utils.loadResourceToString("transform.xslt").toFile());
            Transformer transformer = factory.newTransformer(xslt);

            Source text = new StreamSource(path.toFile());
            transformer.transform(text, new StreamResult(file));

            return file.toPath();
        } catch (TransformerException e) {
            String errorMessage = String.format("Could not transform file: %s. %s", path, e);
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    @Override
    @Transactional
    public List<Integer> xmlToObject(Path path) {
        try {
            List<Integer> list = new ArrayList<>();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(path.toFile());

            NodeList entryNodes = document.getElementsByTagName("entry");
            for (int i = 0; i < entryNodes.getLength(); i++) {
                Element entryElement = (Element) entryNodes.item(i);
                if (Objects.nonNull(entryElement)) {
                    String field = entryElement.getAttribute("field");
                    list.add(extractInteger(field));
                }
            }

            return list;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            String errorMessage = String.format("Could not transform file: %s. %s", path, e);
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private Integer extractInteger(String value) {
        try {
            return value.isEmpty() ? null : Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            log.info("Node does not contain a valid Integer: {}. {}", value, e);
        }

        return null;
    }
}