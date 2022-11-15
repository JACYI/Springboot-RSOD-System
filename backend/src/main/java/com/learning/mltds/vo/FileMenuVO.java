package com.learning.mltds.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * FileMenuVO 类表示某个后端路径下的文件夹和文件信息
 * root 表示路径字符串
 * list 表示文件及文件夹列表的名字信息，使用isDir属性区分是否是文件夹
 */

@Data
@NoArgsConstructor
public class FileMenuVO {
    private String root;
    private List<MenuFiles> list;
    public FileMenuVO(String _root) {
        this.root = _root;
        this.list = new ArrayList<>();
    }
    public void addMenuFile(Boolean _isDir, String _fileName) {
        this.list.add(MenuFiles.builder()
                .isDir(_isDir)
                .fileName(_fileName)
                .build()
        );
    }

}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class MenuFiles {
    private Boolean isDir;
    private String fileName;
}

