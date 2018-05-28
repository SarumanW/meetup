package com.meetup.meetup.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.meetup.meetup.entity.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PdfCreateService {
    private static Logger log = LoggerFactory.getLogger(PdfCreateService.class);

    public void createPDF(List<Event> events) {
        log.debug("Try to create pdf file");
        Document doc = new Document();
        try {
            PdfWriter.getInstance(doc, new FileOutputStream("events.pdf"));
            doc.open();
            PdfPTable table = new PdfPTable(4);
            addTableHeader(table);
            for (Event e : events) {
                addRows(table, e);
            }
            doc.add(table);
            doc.close();
        } catch (DocumentException | FileNotFoundException e) {
            log.error("Cant create pdf file");
        }
        log.debug("Creating pdf file successful");
    }

    private void addRows(PdfPTable table, Event event) {
        table.addCell(event.getName());
        table.addCell(event.getDescription());
        table.addCell(event.getEventDate());
        table.addCell(event.getPeriodicity().name());
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("NAME", "DESCRIPTION", "DATE", "PERIODICITY")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

}
