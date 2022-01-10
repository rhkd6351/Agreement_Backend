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
     * @param mf multipart-File 형식 pdf 파일입니다.
     * @return PdfVO 등록된 PDF 정보
     * @throws IOException pdf file IO 오류
     * @throws NotSupportedException pdf 이외의 확장자
     * @throws NoSuchFileException multipartFile 오류
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
     * @param pdfName pdf의 이름을 입력받습니다.
     * @return pdf byte
     * @throws NotFoundException 유효하지 않은 pdf name
     * @throws IOException pdf file IO 오류
     */
    @GetMapping(path = "/project/pdf/{pdf-name}", produces = MediaType.APPLICATION_PDF_VALUE)
//    @PreAuthorize("hasRole('ROLE_USER')")
    public byte[] getPdf(@PathVariable(name = "pdf-name") String pdfName) throws NotFoundException, IOException {
        return pdfService.getByteByName(pdfName);
    }
}







