package com.learning.mltds.controller;

import com.learning.mltds.controller.dto.PathDTO;
import com.learning.mltds.utils.FileUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.nio.file.Path;
import java.util.Map;

/**
 * 服务器目录和文件相关类
 */

@RestController
@RequestMapping("/file-menu")
public class FileController {
    // 每次打开的最初网址
    private static final Path settingRoot = Paths.get("/");

    @PostMapping("/")
    public Map<String, List<String>> get_menu(@RequestBody PathDTO pathDTO) {
        Path searchDir = null;
        Path rootDir = pathDTO.getRootDir();
        if(rootDir.toString().equals(""))
            pathDTO.setRootDir(settingRoot);

        if(pathDTO.getOperation().equals("preDir"))
            searchDir = rootDir.getParent();
        else if (pathDTO.getOperation().equals("nextDir")){
            if (pathDTO.getSelectedFilename().equals(""))
                searchDir = rootDir;
            else
                searchDir = Paths.get(rootDir.toString(), pathDTO.getSelectedFilename());
        }
        else
            searchDir = settingRoot;

        // list files and return
        return FileUtils.listFiles(searchDir.toString());
    }
}
