package com.curioud.signclass.controller;


import com.curioud.signclass.domain.project.PdfVO;
import com.curioud.signclass.dto.PdfDTO;
import com.curioud.signclass.service.etc.PdfService;
import com.curioud.signclass.util.ObjectConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.NotSupportedException;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    PdfService pdfService;

    @GetMapping("/file/pdf")
    public ResponseEntity<PdfDTO> savePdf(
            @RequestParam("pdf") MultipartFile mf) throws IOException, NotSupportedException {

        PdfVO savedPdf = pdfService.save(mf);
        PdfDTO pdfDTO = ObjectConverter.PdfVOToDTO(savedPdf);

        return new ResponseEntity<>(pdfDTO, HttpStatus.OK);
    }


}
