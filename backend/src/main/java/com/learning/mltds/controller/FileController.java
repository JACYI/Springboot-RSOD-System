package com.learning.mltds.controller;

import com.learning.mltds.controller.dto.PathDTO;
import com.learning.mltds.dto.ObjectinfoDTO;
import com.learning.mltds.entity.Imageinfo;
import com.learning.mltds.entity.Task;
import com.learning.mltds.mapper.ObjectinfoMapper;
import com.learning.mltds.service.IImageinfoService;
import com.learning.mltds.service.IObjectinfoService;
import com.learning.mltds.service.ITaskService;
import com.learning.mltds.utils.FileUtils;
import com.learning.mltds.utils.ResUtils;
import com.learning.mltds.utils.geoserver.TiffDataset;
import com.learning.mltds.vo.FileMenuVO;
import org.gdal.gdal.Dataset;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 服务器目录和文件相关类
 */

@RestController
@RequestMapping("/file-menu")
public class FileController {
    @Resource
    private IImageinfoService imageinfoService;
    @Resource
    private ITaskService taskService;
    @Resource
    private IObjectinfoService objectinfoService;
    @Resource
    private ObjectinfoMapper objectinfoMapper;

    // 每次打开的最初网址
    private static final Path settingRoot = Paths.get("/");
    // 前端查询目录下的文件夹信息
    @PostMapping("/")
    public FileMenuVO getMenu(@RequestBody PathDTO pathDTO) {
        Path searchDir = null;

        String test = pathDTO.getSelectedFilename();

        Path rootDir = pathDTO.getRootDir();
        if(rootDir.toString().equals(""))
            pathDTO.setRootDir(settingRoot);

        if(pathDTO.getOperation().equals("preDir"))
            searchDir = rootDir.getParent();
        else if (pathDTO.getOperation().equals("nextDir")){
            if (pathDTO.getSelectedFilename() == null || pathDTO.getSelectedFilename().equals(""))
                searchDir = rootDir;
            else
                searchDir = Paths.get(rootDir.toString(), pathDTO.getSelectedFilename());
        }
        else
            searchDir = settingRoot;

        // list files and return
        return FileUtils.listFiles(searchDir.toString());
    }

    // 从服务器（后端）上传图像信息到数据库
    @PostMapping("/upload_image_from_server")
    public Map<String, Object> uploadImageFromServer(@RequestBody Map<String, Object> requestBody) {
        // convert
        String imagePath = (String) requestBody.get("imagePath");

        // TODO windows转换功能在Linux系统不需要
        imagePath = linux2WindowsPath(imagePath);
        System.out.println(imagePath);

        TiffDataset dataset = new TiffDataset(imagePath);
        Imageinfo imageinfo = dataset.getImageinfo();

        // 由于 imageinfo 的外键包含task_id，因此需要创建对应的task任务
        Task task = new Task();
        task.setIdentifier("bigship_upload");
        task.setName("NSSC_Bigship");
        task.setCategory("upload");
        task.setStatus("SUCCESS");
        task.setDetectionFilename(imageinfo.getFilename());
        task.setProgress(100.);
        task.setPriority(0);
        task.setInfo("上传图像完成");
        task.setParentTaskId(-1);
        task.setIsConfirmed(true);
        task.setIsDeleted(0);
        // TODO 添加user信息
        task.setTaskUserId(1);

        // 保存新的任务
        try {
            if (!taskService.save(task)) {
                System.out.println("保存任务失败");
                throw new Exception("保存任务失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResUtils.makeResponse("Error", "保存任务失败");
        }
        // 保存imageinfo
//        taskService.
        imageinfo.setTaskId(task.getId());
        try {
            if (!imageinfoService.save(imageinfo)) {
                System.out.println("保存任务失败");
                throw new Exception("保存任务失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResUtils.makeResponse("Error", "保存任务失败");
        }

        // load txt
        File txtFile = new File(imagePath + "\\" + imageinfo.getFilename() + ".txt");
        List<ObjectinfoDTO> objectinfoDTOS = new ArrayList<>();

        if(txtFile.exists()){
            try {
                FileInputStream fileInputStream = new FileInputStream(txtFile);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                StringBuffer buffer = new StringBuffer();
                String line = null;
                while((line = reader.readLine()) != null) {
                    // TODO 读取txt
                    ObjectinfoDTO objectinfoDTO = ObjectinfoDTO.builder()

                            .build();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        objectinfoService.saveObjectinfoDTOS(objectinfoDTOS);

        return null;
    }
    // window系统使用
    private String linux2WindowsPath(String path){
        return path.replace('/', '\\');
    }
}
