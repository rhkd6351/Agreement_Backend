package com.curioud.signclass.service.submittee;

import com.curioud.signclass.domain.project.PdfVO;
import com.curioud.signclass.domain.submittee.SubmitteePdfVO;
import com.curioud.signclass.dto.project.PdfDTO;
import com.curioud.signclass.dto.submittee.SubmitteePdfDTO;
import com.curioud.signclass.repository.project.PdfRepository;
import com.curioud.signclass.repository.submittee.SubmitteePdfRepository;
import com.curioud.signclass.util.FileUtil;
import javassist.NotFoundException;
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
public class SubmitteePdfService {

    private static final String UPLOADPATH = "/pdf";

    SubmitteePdfRepository submitteePdfRepository;
    FileUtil fileUtil;
    int maxFileSize;

    public SubmitteePdfService(@Value("${static.max-file-size}") int maxFileSize, SubmitteePdfRepository submitteePdfRepository, FileUtil fileUtil) {
        this.submitteePdfRepository = submitteePdfRepository;
        this.fileUtil = fileUtil;
        this.maxFileSize = maxFileSize;
    }

    @Transactional
    public SubmitteePdfVO save(SubmitteePdfVO submitteePdfVO){
        return submitteePdfRepository.save(submitteePdfVO);
    }

    @Transactional
    public SubmitteePdfVO save(SubmitteePdfDTO submitteePdfDTO) throws NotFoundException {

        SubmitteePdfVO submitteePdfVO;

        if(submitteePdfDTO.getIdx() == null){
            submitteePdfVO = SubmitteePdfVO.builder()
                    .name(submitteePdfDTO.getName())
                    .originalName(submitteePdfDTO.getOriginalName())
                    .saveName(submitteePdfDTO.getSaveName())
                    .size(submitteePdfDTO.getSize())
                    .totalPage(submitteePdfDTO.getTotalPage())
                    .uploadPath(submitteePdfDTO.getUploadPath())
                    .build();
        }else{
            throw new UnsupportedOperationException("pdf is already uploaded");
        }

        return this.save(submitteePdfVO);
    }

    @Transactional
    public SubmitteePdfVO save(MultipartFile mf) throws NotSupportedException, IOException {

        int i = Objects.requireNonNull(mf.getOriginalFilename()).lastIndexOf(".");
        //TODO 확장자 없는 파일 업로드시 예외처리 어떻게되나 확인하기
        String extension = mf.getOriginalFilename().substring(i);

        if(!extension.equals(".pdf"))
            throw new NotSupportedException("not supported extension : " + extension);

        if (mf.getSize() > maxFileSize) //10메가 용량제한
            throw new NotSupportedException("file size exceed");

        UUID saveName = UUID.randomUUID();
        File savedFile = fileUtil.saveFile(mf, saveName + extension, UPLOADPATH);

        PDDocument doc = PDDocument.load(savedFile);

        SubmitteePdfVO submitteePdf = SubmitteePdfVO.builder()
                .name(saveName.toString())
                .originalName(mf.getOriginalFilename())
                .saveName(saveName + extension)
                .size(mf.getSize())
                .totalPage(doc.getNumberOfPages())
                .uploadPath(UPLOADPATH)
                .extension(extension)
                .build();

        doc.close();
        return this.save(submitteePdf);
    }

    @Transactional(readOnly = true)
    public SubmitteePdfVO getByName(String name) throws NotFoundException {
        Optional<SubmitteePdfVO> optional = submitteePdfRepository.findOneByName(name);

        if(optional.isEmpty())
            throw new NotFoundException("Invalid submittee pdf name");

        return optional.get();
    }

    @Transactional(readOnly = true)
    public SubmitteePdfVO getByIdx(Long idx) throws NotFoundException {
        Optional<SubmitteePdfVO> optional = submitteePdfRepository.findById(idx);

        if(optional.isEmpty())
            throw new NotFoundException("Invalid submittee pdf idx");

        return optional.get();
    }

    @Transactional(readOnly = true)
    public byte[] getByteByName(String name) throws NotFoundException, IOException {

        SubmitteePdfVO pdf = this.getByName(name);

        return fileUtil.getFile(pdf);
    }

}
