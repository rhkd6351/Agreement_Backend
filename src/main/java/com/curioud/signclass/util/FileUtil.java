package com.curioud.signclass.util;


import com.curioud.signclass.domain.project.PdfVO;
import com.curioud.signclass.domain.submittee.SubmitteeObjectSignImgVO;
import com.curioud.signclass.dto.submittee.SubmitteeObjectSignImgDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class FileUtil {

    private final String originPath;

    public FileUtil(@Value("${static.path}") String originPath) {
        this.originPath = originPath;
    }

    public byte[] getFile(PdfVO fvo) throws IOException {
        File file = new File(originPath + fvo.getUploadPath() +"/"+ fvo.getSaveName());
        byte[] byfile = null;

        byfile = Files.readAllBytes(file.toPath());

        return byfile;
    }

    public byte[] getFile(SubmitteeObjectSignImgVO ivo) throws IOException {
        File file = new File(originPath + ivo.getUploadPath() +"/"+ ivo.getSaveName());
        byte[] byfile = null;

        byfile = Files.readAllBytes(file.toPath());

        return byfile;
    }

    public File saveFile(MultipartFile multipartFile, String name, String uploadPath) throws IOException {

        File file = new File(originPath + uploadPath + "/" + name);

        multipartFile.transferTo(file);
        return file;
    }
}
