package com.learning.mltds.controller.dto;

import lombok.Data;

import java.nio.file.Path;

//enum PathOperation {
//    preDir,
//    nextDir
//}

@Data
public class PathDTO {
    private String operation;
    private Path rootDir;
    private String selectedFilename;
}
