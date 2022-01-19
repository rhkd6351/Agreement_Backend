package com.curioud.signclass.domain.project;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Disposition{

    @NotEmpty
    @Column(name = "x_position", nullable = false)
    private int xPosition;

    @NotEmpty
    @Column(name = "y_position", nullable = false)
    private int yPosition;

    @NotEmpty
    @Column(name = "width", nullable = false)
    private int width;

    @NotEmpty
    @Column(name = "height", nullable = false)
    private int height;

    @NotEmpty
    @Column(name = "rotate", nullable = false)
    private float rotate;

    @Column(name = "page", nullable = false)
    private int page;

    @Builder
    public Disposition(int xPosition, int yPosition, int width, int height, float rotate, int page) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.rotate = rotate;
        this.page = page;
    }

}
