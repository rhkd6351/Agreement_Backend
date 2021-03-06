package com.curioud.signclass.service.project;

import com.curioud.signclass.domain.project.PdfVO;
import com.curioud.signclass.dto.project.PdfDTO;
import com.curioud.signclass.repository.project.PdfRepository;
import com.curioud.signclass.util.FileUtil;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.NotSupportedException;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class PdfService {

    PdfRepository pdfRepository;
    FileUtil fileUtil;
    int maxFileSize;

    public PdfService(@Value("${static.max-file-size}") int maxFileSize, PdfRepository pdfRepository, FileUtil fileUtil) {
        this.pdfRepository = pdfRepository;
        this.fileUtil = fileUtil;
        this.maxFileSize = maxFileSize;
    }

    @Transactional
    public PdfVO save(PdfVO pdfVO){
        return pdfRepository.save(pdfVO);
    }

    @Transactional
    public PdfVO save(PdfDTO pdfDTO) throws NotFoundException {

        PdfVO pdfVO;

        if(pdfDTO.getIdx() == null){
            pdfVO = PdfVO.builder()
                    .name(pdfDTO.getName())
                    .originalName(pdfDTO.getOriginalName())
                    .saveName(pdfDTO.getSaveName())
                    .size(pdfDTO.getSize())
                    .totalPage(pdfDTO.getTotalPage())
                    .uploadPath(pdfDTO.getUploadPath())
                    .build();
        }else{
            throw new UnsupportedOperationException("you can't update exist project object");
        }

        return this.save(pdfVO);
    }

    @Transactional
    public PdfVO save(MultipartFile mf) throws NotSupportedException, IOException {

        String extension = "." + Objects.requireNonNull(mf.getContentType()).split("/")[1];

        if(!extension.equals(".pdf"))
            throw new NotSupportedException("not supported extension : " + extension);

        if (mf.getSize() > maxFileSize) //10?????? ????????????
            throw new NotSupportedException("file size exceed");

        UUID saveName = UUID.randomUUID();
        File savedFile = fileUtil.saveFile(mf, saveName + extension, "/pdf");

        PDDocument doc = PDDocument.load(savedFile);

        PdfVO pdf = PdfVO.builder()
                .name(saveName.toString())
                .originalName(mf.getOriginalFilename())
                .saveName(saveName + extension)
                .size(mf.getSize())
                .totalPage(doc.getNumberOfPages())
                .uploadPath("/pdf")
                .extension(extension)
                .build();

        doc.close();
        return this.save(pdf);
    }

    @Transactional(readOnly = true)
    public PdfVO getByName(String name) throws NotFoundException {
        Optional<PdfVO> pdfOptional = pdfRepository.findOneByName(name);

        if(pdfOptional.isEmpty())
            throw new NotFoundException("Invalid pdf name");

        return pdfOptional.get();
    }

    @Transactional(readOnly = true)
    public byte[] getByteByName(String name) throws NotFoundException, IOException {

        PdfVO pdf = this.getByName(name);

        return fileUtil.getFile(pdf);
    }
}
