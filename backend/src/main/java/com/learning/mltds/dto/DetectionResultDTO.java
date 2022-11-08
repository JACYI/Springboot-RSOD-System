package com.learning.mltds.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetectionResultDTO {
    private String imageName;
    private ImageinfoDTO imageinfoDTO;
    private List<ObjectinfoDTO> objectinfoList;
}
