package com.curioud.signclass.service.project;



import com.curioud.signclass.domain.project.ProjectObjectVO;
import com.curioud.signclass.domain.user.UserVO;
import com.curioud.signclass.repository.project.ProjectObjectRepository;
import com.curioud.signclass.service.user.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.util.Optional;

@Service
public class ProjectObjectService {

    @Autowired
    ProjectObjectRepository projectObjectRepository;

    @Autowired
    UserService userService;


    public ProjectObjectVO getByIdx(Long idx) throws NotFoundException {
        Optional<ProjectObjectVO> optional = projectObjectRepository.findById(idx);

        if(optional.isEmpty())
            throw new NotFoundException("invalid project object idx");

        return optional.get();
    }

    public void remove(ProjectObjectVO vo) throws AuthException {

        UserVO user = userService.getMyUserWithAuthorities();

        if(vo.getProject().getUser() != user)
            throw new AuthException("not your own project");

        projectObjectRepository.delete(vo);
    }

}
