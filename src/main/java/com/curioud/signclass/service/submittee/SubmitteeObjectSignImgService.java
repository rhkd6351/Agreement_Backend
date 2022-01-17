package com.curioud.signclass.service.submittee;

import com.curioud.signclass.domain.submittee.SubmitteeObjectSignImgVO;
import com.curioud.signclass.repository.submittee.SubmitteeObjectSignImgRepository;
import com.curioud.signclass.util.FileUtil;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubmitteeObjectSignImgService {
    private static final String UPLOADPATH = "/img";

    SubmitteeObjectSignImgRepository submitteeObjectSignImgRepository;

    FileUtil fileUtil;
    int maxFileSize;

    public SubmitteeObjectSignImgService(SubmitteeObjectSignImgRepository submitteeObjectSignImgRepository, FileUtil fileUtil, @Value("${static.max-file-size}") int maxFileSize) {
        this.submitteeObjectSignImgRepository = submitteeObjectSignImgRepository;
        this.fileUtil = fileUtil;
        this.maxFileSize = maxFileSize;
    }

    @Transactional
    public SubmitteeObjectSignImgVO save(MultipartFile mf) throws NotSupportedException, IOException {

        String extension = Objects.requireNonNull(mf.getContentType()).split("/")[1];

        if(!(extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("bmp")))
            throw new NotSupportedException("not supported extension : " + extension);

        if (mf.getSize() > maxFileSize) //10메가 용량제한
            throw new NotSupportedException("file size exceed");

        UUID saveName = UUID.randomUUID();
        fileUtil.saveFile(mf, saveName + extension, UPLOADPATH);

        SubmitteeObjectSignImgVO vo = SubmitteeObjectSignImgVO.builder()
                .name(saveName.toString())
                .originalName(mf.getOriginalFilename())
                .saveName(saveName + extension)
                .size(mf.getSize())
                .uploadPath(UPLOADPATH)
                .extension(extension)
                .build();

        return submitteeObjectSignImgRepository.save(vo);
    }

    @Transactional(readOnly = true)
    public byte[] getByteByName(String name) throws NotFoundException, IOException {

        SubmitteeObjectSignImgVO img = this.getByName(name);

        return fileUtil.getFile(img);
    }

    @Transactional(readOnly = true)
    public SubmitteeObjectSignImgVO getByName(String name) throws NotFoundException {

        Optional<SubmitteeObjectSignImgVO> optional = submitteeObjectSignImgRepository.findByName(name);

        if(optional.isEmpty())
            throw new NotFoundException("invalid image name");

        return optional.get();
    }



}
