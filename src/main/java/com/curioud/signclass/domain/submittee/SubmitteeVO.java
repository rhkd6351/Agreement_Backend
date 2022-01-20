package com.curioud.signclass.domain.submittee;

import com.curioud.signclass.domain.project.PdfVO;
import com.curioud.signclass.domain.project.ProjectObjectVO;
import com.curioud.signclass.domain.project.ProjectVO;
import com.curioud.signclass.dto.project.PdfDTO;
import com.curioud.signclass.dto.submittee.SubmitteeDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "SUBMITTEE_TB")
public class SubmitteeVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", updatable = false)
    private Long idx;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "student_name", length = 45, nullable = false)
    private String studentName;

    @Column(name = "student_id", nullable = false)
    private int studentId;

    @Column(name = "activated", nullable = false)
    private int activated;

    @Column(name = "reg_date")
    @CreationTimestamp
    private LocalDateTime regDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_idx_fk", nullable = false)
    private ProjectVO project;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submittee_pdf_idx_fk", nullable = false)
    private SubmitteePdfVO submitteePdf;

    @OneToMany(mappedBy = "submittee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubmitteeObjectVO> submitteeObjects;


    @Builder
    public SubmitteeVO(String name, String studentName, int studentId, int activated, ProjectVO project, SubmitteePdfVO submitteePdf) {
        this.name = name;
        this.studentName = studentName;
        this.studentId = studentId;
        this.activated = activated;
        this.project = project;
        this.submitteePdf = submitteePdf;
        submitteeObjects = new ArrayList<>();
    }

    public SubmitteeDTO dto(Boolean objects, PdfVO pdf){

        List<SubmitteeObjectSignVO> submitteeObjectSigns = new ArrayList<>();
        List<SubmitteeObjectTextVO> submitteeObjectTexts = new ArrayList<>();
        List<SubmitteeObjectCheckboxVO> submitteeObjectCheckboxes = new ArrayList<>();

        if(objects){
            for(SubmitteeObjectVO vo : submitteeObjects){
                if(vo instanceof SubmitteeObjectSignVO)
                    submitteeObjectSigns.add((SubmitteeObjectSignVO) vo);
                else if(vo instanceof SubmitteeObjectTextVO)
                    submitteeObjectTexts.add((SubmitteeObjectTextVO) vo);
                else if(vo instanceof SubmitteeObjectCheckboxVO)
                    submitteeObjectCheckboxes.add((SubmitteeObjectCheckboxVO) vo);
            }
        }

        return SubmitteeDTO.builder()
                .idx(idx)
                .name(name)
                .studentName(studentName)
                .studentId(studentId)
                .activated(activated)
                .regDate(regDate)
                .submitteeObjectSigns(submitteeObjectSigns.stream().map(SubmitteeObjectSignVO::dto).collect(Collectors.toList()))
                .submitteeObjectTexts(submitteeObjectTexts.stream().map(SubmitteeObjectTextVO::dto).collect(Collectors.toList()))
                .submitteeObjectCheckboxes(submitteeObjectCheckboxes.stream().map(SubmitteeObjectCheckboxVO::dto).collect(Collectors.toList()))
                .pdf(pdf != null ? pdf.dto() : new PdfDTO())
                .build();
    }

    public void addObject(SubmitteeObjectVO vo) {
        this.getSubmitteeObjects().add(vo);
        vo.setSubmittee(this);
    }

    public void addAllObjects(List<SubmitteeObjectVO> objects) {
        for (SubmitteeObjectVO vo : objects) this.addObject(vo);
    }








}

//    @OneToMany(mappedBy = "submittee")
//    Set<SubmitteeObjectCheckboxVO> submitteeObjectCheckboxes;
//
//    @OneToMany(mappedBy = "submittee")
//    Set<SubmitteeObjectTextVO> submitteeObjectTexts;
//
//    @OneToMany(mappedBy = "submittee")
//    List<SubmitteeObjectSignVO> submitteeObjectSigns;
