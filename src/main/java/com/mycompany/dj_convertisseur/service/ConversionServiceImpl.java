/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dj_convertisseur.service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.Sheet; // Kòrèk: ss olye de sl
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author StHilaireDjoune
 */
@Service
public class ConversionServiceImpl implements ConversionService{

private final String uploadPath = "src/main/resources/static/files/";

    @Override
    public String convertToWord(MultipartFile file) throws Exception {
        checkFolder();
        String fileName = UUID.randomUUID().toString() + ".docx";
        
        try (PDDocument pdf = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(pdf);

            try (XWPFDocument doc = new XWPFDocument()) {
                XWPFParagraph p = doc.createParagraph();
                XWPFRun run = p.createRun();
                run.setText(text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", ""));

                try (FileOutputStream out = new FileOutputStream(uploadPath + fileName)) {
                    doc.write(out);
                }
            }
        }
        return "/files/" + fileName;
    }

    @Override
    public String convertToExcel(MultipartFile file) throws Exception {
        checkFolder();
        String fileName = UUID.randomUUID().toString() + ".xlsx";

        try (PDDocument pdf = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(pdf);

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Data PDF");
                String[] lines = text.split("\\r?\\n");

                for (int i = 0; i < lines.length; i++) {
                    Row row = sheet.createRow(i);
                    row.createCell(0).setCellValue(lines[i]);
                }

                try (FileOutputStream out = new FileOutputStream(uploadPath + fileName)) {
                    workbook.write(out);
                }
            }
        }
        return "/files/" + fileName;
    }

    @Override
    public String convertToPdf(MultipartFile file) throws Exception {
        checkFolder();
        String fileName = UUID.randomUUID().toString() + ".pdf";
        String fullPath = uploadPath + fileName;

        // --- Lojik Konvèsyon Word to PDF (ki te nan Servlet la) ---
        File tempDocx = File.createTempFile("temp_upload_", ".docx");
        file.transferTo(tempDocx);

        try {
            // Chaje dokiman Word la ak Docx4J
            WordprocessingMLPackage wordMLPackage = Docx4J.load(tempDocx);
            
            // Kreye fichiye PDF a
            try (FileOutputStream os = new FileOutputStream(fullPath)) {
                Docx4J.toPDF(wordMLPackage, os);
            }
        } finally {
            // Toujou efase fichiye tanporè a
            if (tempDocx.exists()) {
                tempDocx.delete();
            }
        }
        
        return "/files/" + fileName;
    }

    private void checkFolder() {
        File folder = new File(uploadPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}