/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.dj_convertisseur.service;

import org.springframework.web.multipart.MultipartFile;
/**
 *
 * @author StHilaireDjoune
 */
public interface ConversionService {
    
    String convertToPdf(MultipartFile file) throws Exception;
    String convertToWord(MultipartFile file) throws Exception;
    String convertToExcel(MultipartFile file) throws Exception;
}

