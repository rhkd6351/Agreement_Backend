package com.curioud.signclass.service.submittee;

import com.curioud.signclass.domain.submittee.SubmitteePdfVO;
import com.curioud.signclass.repository.submittee.SubmitteePdfRepository;
import com.curioud.signclass.util.FileUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.NotSupportedException;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
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

        return submitteePdfRepository.save(submitteePdf);
    }

}
