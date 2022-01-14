package com.curioud.signclass.controller;


import com.curioud.signclass.domain.project.PdfVO;
import com.curioud.signclass.dto.project.PdfDTO;
import com.curioud.signclass.service.project.PdfService;
import com.curioud.signclass.util.ObjectConverter;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

@RestController
@RequestMapping("/api")
public class FileController {

    PdfService pdfService;
    String serverUrl;
    ObjectConverter objectConverter;

    public FileController(PdfService pdfService, @Value("${server.url}") String serverUrl, ObjectConverter objectConverter) {
        this.pdfService = pdfService;
        this.serverUrl = serverUrl;
        this.objectConverter = objectConverter;
    }

    /**
     *
     * @param mf pdf file
     * @return 저장된 pdf 정보
     * @throws IOException 파일 입출력 오류
     * @throws NotSupportedException 승인되지 않은 파일 확장자
     */
    @PostMapping("/project/pdf")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<PdfDTO> savePdf(
            @RequestParam(value = "file_pdf") MultipartFile mf) throws IOException, NotSupportedException {

        if(mf.isEmpty())
            throw new NoSuchFileException("Empty file"); //TODO 예외 바꾸기

        PdfVO savedPdf = pdfService.save(mf);
        PdfDTO pdfDTO = objectConverter.pdfVOToDTO(savedPdf);

        return new ResponseEntity<>(pdfDTO, HttpStatus.OK);
    }


    /**
     *
     * @param pdfName pdf의 saveName
     * @return pdf file
     * @throws NotFoundException 존재하지 않는 pdf 파일(DB)
     * @throws IOException 파일 입출력 오류
     */
    @GetMapping(path = "/project/pdf/{pdf-name}", produces = MediaType.APPLICATION_PDF_VALUE)
//    @PreAuthorize("hasRole('ROLE_USER')")
    public byte[] getPdf(@PathVariable(name = "pdf-name") String pdfName) throws NotFoundException, IOException {
        return pdfService.getByteByName(pdfName);
    }
}







