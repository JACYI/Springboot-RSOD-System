package com.learning.mltds.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.learning.mltds.config.CommonConfig;
import com.learning.mltds.entity.Imageinfo;
import com.learning.mltds.service.IImageinfoService;
import com.learning.mltds.utils.FileUtils;
import com.learning.mltds.utils.ResUtils;
import com.learning.mltds.utils.geoserver.GeoServerUtil;
import com.learning.mltds.utils.geoserver.ImageExtConvert;
import com.learning.mltds.utils.geoserver.TiffDataset;
import com.learning.mltds.vo.ImageinfoVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.awt.*;
import java.io.File;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/viewer")
public class MapViewerController {
    // 常规图像后缀名集合
    private static final Set<String> nomarlSet = new HashSet<String>(Arrays.asList("jpg", "png", "jpeg"));
    // Giff地理图像后缀名集合
    private static final Set<String> RSSet = new HashSet<String>(Arrays.asList("tif", "tiff"));
    @Resource
    private IImageinfoService imageinfoService;

    @PostMapping("judge-whether-doing-datatype-convert/")
    public Map<String, Object> judgeWhetherDoingDatatypeConvert(@RequestBody Map<String, Object> requestBody) {
        String imagePath = (String) requestBody.get("imagePath");

        // TODO 将Linux原图像路径映射到windows
        if(imagePath.lastIndexOf("/") != -1)
            // linux路径，转化为windows
            imagePath = Paths.get(CommonConfig.sourceImagePath,
                    imagePath.substring(imagePath.lastIndexOf("/") + 1)).toString();

        File imageFile;
        if(imagePath == null || !(imageFile = new File(imagePath)).exists())
            return ResUtils.makeResponse("Error", "图像文件不存在");
        String imageName = imageFile.getName();
        // 文件名和扩展名提取
        String filename = imageName.substring(0, imageName.lastIndexOf('.'));
        String imageExt = imageName.substring(imageName.lastIndexOf('.') + 1).toLowerCase();

        // 该图像不属于 常规图像 和 遥感图像，报错返回
        if(!nomarlSet.contains(imageExt) && !RSSet.contains(imageExt))
            return ResUtils.makeResponse("Error", "不支持的图像扩展名");

        if(RSSet.contains(imageExt)){
            // TODO 对 SAR 遥感图像进行类型转换
//            TiffDataset dataset = new TiffDataset(imagePath);
//            if(dataset.)
            return ResUtils.makeResponse(new HashMap<String, Object>(){{
                put("hint_info", "正在打开图像，请稍等...");
            }});
        }
        else
            return ResUtils.makeResponse(new HashMap<String, Object>(){{
                put("hint_info", "正在打开图像，请稍等...");
            }});

    }

    @PostMapping("wms-service/")
    public Map<String, Object> publishWnsService(@RequestBody Map<String, Object> requestBody) {
        String imagePath = (String) requestBody.get("imagePath");

        // TODO 将Linux原图像路径映射到windows
        if(imagePath.lastIndexOf("/") != -1)
            // linux路径，转化为windows
            imagePath = Paths.get(CommonConfig.sourceImagePath,
                    imagePath.substring(imagePath.lastIndexOf("/") + 1)).toString();

        File imageFile;
        if(imagePath == null || !(imageFile = new File(imagePath)).exists())
            return ResUtils.makeResponse("Error", "图像文件不存在");
        String imageName = imageFile.getName();
        // 图像的 文件名 和 扩展名 提取
        String filename = imageName.substring(0, imageName.lastIndexOf('.'));
        String fileExt = imageName.substring(imageName.lastIndexOf('.') + 1).toLowerCase();


        ImageinfoVO imageinfoVO = null;
        // 先从数据库读取
        QueryWrapper<Imageinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted" , 0);
        queryWrapper.eq("filename", filename);
        Imageinfo imageinfo = imageinfoService.getOne(queryWrapper);

        // 若数据库不存在，妥协的方法是从gdal中读取相关信息
        if(imageinfo != null) {
            imageinfoVO = imageinfo.convert2VO();
        } {
            TiffDataset dataset = new TiffDataset(imagePath);
            imageinfoVO = dataset.getImageinfo();
        }
        if (RSSet.contains(fileExt)) {

            if(imageinfoVO.getSensorType().equals("SAR")) {
                // TODO 处理SAR图像
                String compressedTiffPath = new String(filename + "_compressed" + fileExt);
                imagePath = compressedTiffPath;
            }
        }
        else if(nomarlSet.contains(fileExt)) {
            // 属于普通图像，加入模拟的地理信息将其转化为遥感图像tif文件
            String dirName = FileUtils.getDirName(imagePath);
            String imageTiffPath = dirName + "\\" + filename + ".tif";
            // 当前文件夹下已经有对应的tif文件
            if(new File(imageTiffPath).exists())
                imagePath = imageTiffPath;
            else {
                // TODO 图像后缀名转换
                imagePath = ImageExtConvert.nomarl2Tiff(imagePath);
            }
        } else return ResUtils.makeResponse("Error", "不支持的图像扩展名");


        /*--------------------------------------------*
        *                 发布栅格图像                  *
        * --------------------------------------------*/
        String workspace = null;
        try {
            // storeName 用于控制发布的数据存储名字，一般以图像名命名
//            imagePath = "/home/yiyonghao/geoserver_data/yyh/task-image/" + imageName;
            workspace = GeoServerUtil.publishTiff(imagePath, filename);
        }
        catch (Exception e){
            System.out.println("发布图片失败");
            e.printStackTrace();
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("workspace", workspace);
        responseMap.put("layer_name", filename);
        responseMap.put("image_info", imageinfo==null ? null : imageinfoVO);

        return ResUtils.makeResponse(responseMap);
    }
}
