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
     * @return PdfVO 등록된 PDF의 정보를 리턴합니다.
     * @throws IOException pdf 파일를 저장하는데 오류가 발생하였습니다.
     * @throws NotSupportedException 확장자가 pdf 이외의 타입입니다.
     * @throws NotFoundException 입력받은 변수 mf가 비어있습니다.
     */
    @PostMapping("/project/pdf")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<PdfDTO> savePdf(
            @RequestParam(value = "pdfFile") MultipartFile mf) throws IOException, NotSupportedException, NotFoundException {

        if(mf.isEmpty())
            throw new NotFoundException("Empty file"); //TODO 예외 바꾸기

        PdfVO savedPdf = pdfService.save(mf);
        PdfDTO pdfDTO = objectConverter.PdfVOToDTO(savedPdf);

        return new ResponseEntity<>(pdfDTO, HttpStatus.OK);
    }

    /**
     *
     * @param pdfName pdf의 이름을 입력받습니다.
     * @return pdf file을 byte 형식으로 리턴합니다.
     * @throws NotFoundException pdf name이 유효하지 않습니다.
     * @throws IOException pdf파일을 불러오는데 오류가 발생하였습니다.
     */
    @GetMapping(path = "/project/pdf/{pdf-name}", produces = MediaType.APPLICATION_PDF_VALUE)
//    @PreAuthorize("hasRole('ROLE_USER')")
    public byte[] savePdf(@PathVariable(name = "pdf-name") String pdfName) throws NotFoundException, IOException {
        return pdfService.getByteByName(pdfName);
    }
}







