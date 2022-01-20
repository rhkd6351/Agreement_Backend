package com.curioud.signclass.controller;

import com.curioud.signclass.service.project.PdfService;
import com.curioud.signclass.util.ObjectConverter;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
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
     * @param pdfName pdf의 saveName
     * @return pdf file
     * @throws NotFoundException 존재하지 않는 pdf 파일(DB)
     * @throws IOException 파일 입출력 오류
     */
    @GetMapping(path = "/projects/pdf/{pdf-name}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getPdf(@PathVariable(name = "pdf-name") String pdfName) throws NotFoundException, IOException {
        return pdfService.getByteByName(pdfName);
    }
}







