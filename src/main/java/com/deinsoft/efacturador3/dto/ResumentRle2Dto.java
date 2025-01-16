/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.dto;

/**
 *
 * @author user
 */
public class ResumentRle2Dto {
    ResumentRleDto ResumentRleDto;
    ResumentRleDto ResumentRleDtoBd;

    public ResumentRle2Dto() {
    }

    public ResumentRle2Dto(ResumentRleDto ResumentRleDto, ResumentRleDto ResumentRleDtoBd) {
        this.ResumentRleDto = ResumentRleDto;
        this.ResumentRleDtoBd = ResumentRleDtoBd;
    }

    
    
    public ResumentRleDto getResumentRleDto() {
        return ResumentRleDto;
    }

    public void setResumentRleDto(ResumentRleDto ResumentRleDto) {
        this.ResumentRleDto = ResumentRleDto;
    }

    public ResumentRleDto getResumentRleDtoBd() {
        return ResumentRleDtoBd;
    }

    public void setResumentRleDtoBd(ResumentRleDto ResumentRleDtoBd) {
        this.ResumentRleDtoBd = ResumentRleDtoBd;
    }
    
    
}
