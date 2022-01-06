package com.curioud.signclass.controller;


import com.curioud.signclass.domain.project.PdfVO;
import com.curioud.signclass.dto.PdfDTO;
import com.curioud.signclass.service.project.PdfService;
import com.curioud.signclass.util.FileUtil;
import com.curioud.signclass.util.ObjectConverter;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.NotSupportedException;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    PdfService pdfService;

    @PostMapping("/project/pdf")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<PdfDTO> savePdf(
            @RequestParam("pdf") MultipartFile mf) throws IOException, NotSupportedException {

        PdfVO savedPdf = pdfService.save(mf);
        PdfDTO pdfDTO = ObjectConverter.PdfVOToDTO(savedPdf);

        return new ResponseEntity<>(pdfDTO, HttpStatus.OK);
    }

    @GetMapping(path = "/project/pdf/{pdf-name}", produces = MediaType.APPLICATION_PDF_VALUE)
    @PreAuthorize("hasRole('ROLE_USER')")
    public byte[] savePdf(@PathVariable(name = "pdf-name") String pdfName) throws NotFoundException, IOException {
        return pdfService.getByteByName(pdfName);
    }
}







