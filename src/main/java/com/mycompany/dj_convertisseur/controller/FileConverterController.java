/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dj_convertisseur.controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
/**
 *
 * @author StHilaireDjoune
 */
@Controller
public class FileConverterController {
// Kote n ap sove fichiye yo (Asire folder sa egziste nan pwojè a)
    private final String UPLOAD_DIR = "src/main/resources/static/files/";

    @GetMapping("/")
    public String index() {
        return "index"; // Sa ap louvri paj HTML ou a
    }

    @PostMapping("/convert")
    public String handleConversion(@RequestParam("file") MultipartFile file,
                                   @RequestParam("conversionType") String type,
                                   RedirectAttributes redirectAttributes) {
        
        if (file.isEmpty()) {
            return "redirect:/";
        }

        try {
            File folder = new File(UPLOAD_DIR);
            if (!folder.exists()) folder.mkdirs();

            String outputFileName = "";
            
            switch (type) {
                case "wordToPdf":
                    outputFileName = "converted.pdf";
                    wordToPdfLogic(file, UPLOAD_DIR + outputFileName);
                    break;
                    
                case "pdfToWord":
                    outputFileName = "converted.docx";
                    pdfToWordLogic(file, UPLOAD_DIR + outputFileName);
                    break;
                    
                case "pdfToExcel":
                    outputFileName = "converted.xlsx";
                    pdfToExcelLogic(file, UPLOAD_DIR + outputFileName);
                    break;
            }

            // Voye URL la tounen nan paj la
            redirectAttributes.addFlashAttribute("resultFileUrl", "/files/" + outputFileName);

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Erè pandan konvèsyon an: " + e.getMessage());
        }

        return "redirect:/";
    }

    // --- LOGIK KONVÈSYON YO ---

    private void wordToPdfLogic(MultipartFile file, String dest) throws Exception {
        File tempDocx = File.createTempFile("temp", ".docx");
        file.transferTo(tempDocx);
        WordprocessingMLPackage wordMLPackage = Docx4J.load(tempDocx);
        try (OutputStream os = new FileOutputStream(dest)) {
            Docx4J.toPDF(wordMLPackage, os);
        }
        tempDocx.delete();
    }

    private void pdfToWordLogic(MultipartFile file, String dest) throws Exception {
        String text = extractText(file);
        try (XWPFDocument doc = new XWPFDocument();
             FileOutputStream fos = new FileOutputStream(dest)) {
            XWPFParagraph para = doc.createParagraph();
            para.createRun().setText(text);
            doc.write(fos);
        }
    }

    private void pdfToExcelLogic(MultipartFile file, String dest) throws Exception {
        String text = extractText(file);
        try (Workbook wb = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(dest)) {
            Sheet sheet = wb.createSheet("Data");
            String[] lines = text.split("\\r?\\n");
            int i = 0;
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    Row row = sheet.createRow(i++);
                    row.createCell(0).setCellValue(line.trim());
                }
            }
            wb.write(fos);
        }
    }

    private String extractText(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream();
             PDDocument doc = PDDocument.load(is)) {
            return new PDFTextStripper().getText(doc);
        }
    }
}