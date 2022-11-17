package com.learning.mltds.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.learning.mltds.config.CommonConfig;
import com.learning.mltds.dto.DetectionResultDTO;
import com.learning.mltds.dto.ImageinfoDTO;
import com.learning.mltds.dto.ObjectinfoDTO;
import com.learning.mltds.entity.Imageinfo;
import com.learning.mltds.entity.Objectinfo;
import com.learning.mltds.entity.Task;
import com.learning.mltds.mapper.ImageinfoMapper;
import com.learning.mltds.mapper.ObjectinfoMapper;
import com.learning.mltds.service.IImageinfoService;
import com.learning.mltds.service.IObjectinfoService;
import com.learning.mltds.service.ITaskService;
import com.learning.mltds.utils.FileUtils;
import com.learning.mltds.utils.ImageUtils;
import com.learning.mltds.utils.ReqUtils;
import com.learning.mltds.utils.ResUtils;
import com.learning.mltds.vo.ObjectinfoVO;
import com.sun.imageio.plugins.common.ImageUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/detection")
public class DetectionController {
    @Resource
    private ITaskService taskService;
    @Resource
    private IImageinfoService imageInfoService;
    @Resource
    private IObjectinfoService objectinfoService;
    @Resource
    private ImageinfoMapper imageinfoMapper;
    @Resource
    private ObjectinfoMapper objectinfoMapper;

//    private static final String SLICE_BASE_PATH = CommonConfig.imageBaseUrl + "\\detection";

    // 保存检测结果
    @PostMapping("/save")
    public Map<String, Object> saveDetectionResult(@RequestBody Map<String, Object> detectionResults) {
//    public Map<String, Object> saveDetectionResult(@RequestBody List<DetectionResultDTO> detectionResults) {

        List<DetectionResultDTO> detectionResultDTOS = ReqUtils.detectionResultsConvert(detectionResults);
        // 待修改，userid通过cookie获取
        Integer userId = 1;

        for(DetectionResultDTO detectionResult:detectionResultDTOS){
            Boolean flag = false;  // 标记是否报错，初始化为false
            Integer currentTaskId = -1;

            // 从resultDTO中读取数据
            ImageinfoDTO imageResult = detectionResult.getImageInfo();
            List<ObjectinfoVO> objectinfoVOS = detectionResult.getObjectInfos();
//            // VO -> DO
//            List<Objectinfo> objectinfos = new ArrayList<>();
//            for(ObjectinfoVO objectinfoVO : objectinfoVOS) {
//                objectinfos.add(objectinfoVO.convert2DO());
//            }
            // 这里的 imageName 不包含目录和后缀名！，等同于 filename
            String imageName = detectionResult.getImageName();

            //判断当前图片是否有其对应的taskId，如果没有那么就从创建一个
            // 判断是否存在任务id
            for(ObjectinfoVO objectinfoVO : objectinfoVOS){
                if(objectinfoVO.getTaskId() != null){
                    currentTaskId = objectinfoVO.getTaskId();
                    break;
                }
            }

            if(currentTaskId == -1){
                // 如果存在对应的图像imageinfo，则从imageinfo记录中提取taskId
                QueryWrapper<Imageinfo> queryWrapper = new QueryWrapper<>();
                // user id TODO
                queryWrapper.eq("filename", imageName);
                queryWrapper.eq("is_deleted", 0);
                Imageinfo curImageinfo = imageInfoService.getOne(queryWrapper);
                if(curImageinfo != null)
                    currentTaskId = curImageinfo.getTaskId();
                else {
                    // 如果当前图片没有对应的taskId，那么就创建一个新的task。如果是标记一张大图，那么其就是没有taskId的
                    Task task = new Task();
                    task.setName("图片标注");
                    task.setCategory("save_result");
                    task.setStatus("SUCCESS");
                    task.setProgress(100.);
                    task.setTaskUserId(userId);
                    task.setIsConfirmed(Boolean.FALSE);
                    try {
                        if(!taskService.save(task))
                            throw new Exception("任务添加失败");
                    } catch (Exception e) {
                        System.out.println("保存结果时，任务添加失败");
                        return ResUtils.makeResponse("Error", "新建Task失败");
                    }
                    currentTaskId = task.getId();
                }
            }

            imageResult.setTaskId(currentTaskId);
            // 做图片以及目标的保存

            // 先删除数据库中旧的image_info，会选择名字相同且is_deleted为0进行删除
            // 删除imageinfo记录的同时关联删除objectinfo对应记录 TODO 不考虑用户的版本，待改进
//            imageInfoService.deleteImageInfo(userId, imageName);
//            imageInfoService.deleteImageInfo(imageName);
            objectinfoMapper.deleteObjectinfoByFilename(imageName);
            imageinfoMapper.deleteImageinfoByFilename(imageName);

            // 保存图片信息到数据库，返回新增的 Imageinfo 数据的 id
//            System.out.println("--------------------------------------");
//            System.out.println(imageResult);
            Integer imageinfoId = imageInfoService.saveImageInfoFromMap(imageResult);
            if(imageinfoId == -1){
                System.out.println("保存结果时，图片添加失败");
                return ResUtils.makeResponse("Error", "保存结果时，图片添加失败");
            }

            // 对objectInfos中的信息做一些补充修正，主要针对自己标的目标
            Imageinfo imageinfo = imageInfoService.getById(imageinfoId);
            String imagePath = imageinfo.getPath(); // 遥感图像原图路径

            // TODO 检测路径映射到 springboot 后端路径
            imagePath = Paths.get(CommonConfig.sourceImagePath,
                    imagePath.split("task-image/")[1]).toString();

            // 待保存切片名称设置
            String imageOnlyName = imageName;
            if(imageName.indexOf('.') != -1){
                imageOnlyName = imageName.substring(0, imageName.indexOf('.'));
            }
            Integer objIndex = 0;
            if(!new File(CommonConfig.SLICE_PATH, userId.toString()).exists()){
                new File(CommonConfig.SLICE_PATH, userId.toString()).mkdirs();
            }

            List<Objectinfo> objectinfos = new ArrayList<>();
            // 调整目标信息 VI -> DO ，并针对手动标注的目标进行信息的补充
            for(ObjectinfoVO objectinfoVO : objectinfoVOS){
                Objectinfo objectinfo = objectinfoVO.convert2DO();
                // 如果目标是重新标记的，那么就需要补充信息
                if(objectinfoVO.getIsLabeled() != null && objectinfoVO.getIsLabeled()){
                    // 标记的设置 confidence 为1.0
                    if(objectinfoVO.getConfidence() == null)
                        objectinfoVO.setConfidence(1.);

                    List<Integer> bbox = objectinfoVO.getBbox();
                    int x = 0, y = 0;
                    for(int i=0; i<4; i++) {
                        x += bbox.get(i*2);
                        y += bbox.get(i*2+1);
                    }
                    objectinfo.setImageCenterX(x/4);
                    objectinfo.setImageCenterY(y/4);

                    Integer mean_x = 0;
                    Integer mean_y = 0;
                    int min_x = Integer.MAX_VALUE;
                    int max_x = Integer.MIN_VALUE;
                    int min_y = Integer.MAX_VALUE;
                    int max_y = Integer.MIN_VALUE;
                    for(int i=0; i<bbox.size(); i+=2){
                        mean_x += bbox.get(i);
                        mean_y += bbox.get(i + 1);
                        min_x = Math.min(min_x, bbox.get(i));
                        max_x = Math.max(max_x, bbox.get(i));
                        min_y = Math.min(min_y, bbox.get(i + 1));
                        max_y = Math.max(max_y, bbox.get(i + 1));
                    }
                    mean_x = mean_x / (bbox.size()  / 2);
                    mean_y = mean_y / (bbox.size()  / 2);

                    // 保存图像切片
                    String slicePath = (new File(CommonConfig.SLICE_PATH, userId.toString())).toString();
                    String targetCropImgPath = (new File(slicePath, imageOnlyName + "_" + objIndex + "_labeled.jpg")).toString();
                    String AreaCropImgPath = (new File(slicePath, imageOnlyName + "_" + objIndex + "_area_labeled.jpg")).toString();
                    ImageUtils.imageCut(min_x, min_y, max_x - min_x, max_y - min_y, imagePath, targetCropImgPath);
                    ImageUtils.imageCut(mean_x - CommonConfig.areaSize / 2, mean_y - CommonConfig.areaSize / 2,
                            CommonConfig.areaSize, CommonConfig.areaSize, imagePath, AreaCropImgPath);
                    objectinfo.setTargetSlicePath(targetCropImgPath);
                    objectinfo.setAreaSlicePath(AreaCropImgPath);
                }


                objectinfo.setTaskId(currentTaskId);
                objectinfo.setImageId(imageinfoId);

                objectinfos.add(objectinfo);
//                 下划线转驼峰,因为在遍历Map的同时添加key会有问题，所以这里写成这样了
//                Set<String> objectInfoKeySet = ;
//                List<String> objectInfoKeyList = new ArrayList<>(object.keySet());
//                for(String key: objectInfoKeyList) {
//                    String camelKey = MapUtils.underlineToCamel(key);
//                    // 如果这里是下划线那么就转成驼峰
//                    if (!objectInfo.containsKey(camelKey)) {
//                        objectInfo.put(camelKey, objectInfo.get(key));
//                        objectInfo.remove(key);
//                    }
//                }

//                objectInfoService.turnObjectInfoSepcialValue(objectInfo);
                // 如果第二次保存此时objectInfo是从数据库中读的，会带有id，需要pop掉，不然会出现主键重复的问题
//                objectInfo.remove("id");
//                System.out.println(objectInfo);
                objIndex += 1;
            }


            // 批量保存目标信息
            try {
                if (objectinfos.size() != 0)
//                    if(!objectinfoService.saveObjectinfoDTOS(objectinfoDTOS))
                    if(!objectinfoService.saveBatch(objectinfos))
                        throw new Exception("保存结果时, 目标添加失败");
            } catch (Exception e) {
                e.printStackTrace();
                return ResUtils.makeResponse("Error", "保存结果时，目标添加失败");
            }

            // TODO 当前结果是有对应的taksID的，说明是检测来的结果或者曾经保存过的结果, 逻辑待确认
            if(currentTaskId != -1){
                // 确认任务
                Task task = taskService.getById(currentTaskId);
                task.setIsConfirmed(Boolean.TRUE);
                try {
                    if(!taskService.updateById(task))
                        throw new Exception("更新Task的Confirm失败");
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResUtils.makeResponse("Error", "更新Task的Confirm失败");
                }
                // TODO 如果当前不为-1，那么缓存中可能存在该信息，需要删除缓存。基础逻辑为先更新数据库再删除缓存，这种策略更好一点
                // redisTaskId = currentTaskId;
            }
            // 保存检测结果到 TXT 文件
            if(!FileUtils.saveDetectionResult2Txt(imageName, objectinfos)) {

                return ResUtils.makeResponse("Error", "保存结果txt失败");
            }
        }


//        System.out.println(requestBody);
        return ResUtils.makeResponse();
    }

    @PostMapping("load_detection_result_from_db/")
    public Map<String, Object> loadDetectionResultFromDB(@RequestBody Map<String, Object> requestBody) {
        String imagePath = (String) requestBody.get("imagePath");

        imagePath = ReqUtils.linux2WindowsPath(imagePath);

        String filename = imagePath.substring(imagePath.lastIndexOf('\\') + 1);
        String imageName = filename.substring(0, filename.lastIndexOf('.'));
        QueryWrapper<Imageinfo> imageinfoQueryWrapper = new QueryWrapper<>();
        imageinfoQueryWrapper.eq("is_deleted", 0);
        imageinfoQueryWrapper.eq("filename", imageName);
        Imageinfo imageOne = imageInfoService.getOne(imageinfoQueryWrapper);

        // TODO 从txt中读取...

        // 从数据库中读取
        QueryWrapper<Objectinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("image_id", imageOne.getId());
        queryWrapper.eq("is_deleted", 0);
        List<Objectinfo> objectinfos = objectinfoService.list(queryWrapper);
        // 将DO转化为VO
        List<ObjectinfoVO> objectinfoVOS = new ArrayList<>();
        for(Objectinfo obj : objectinfos)
            objectinfoVOS.add(obj.convert2VO(imagePath));

        return ResUtils.makeResponse(new HashMap<String, Object>(){{
            put("object_infos", objectinfoVOS);
        }});
    }
}
