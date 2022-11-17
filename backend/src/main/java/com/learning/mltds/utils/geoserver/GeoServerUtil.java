package com.learning.mltds.utils.geoserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.bind.v2.TODO;
import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.decoder.RESTDataStore;
import org.springframework.stereotype.Service;


import java.io.File;
import java.net.URL;
import java.util.*;

@Service
public class GeoServerUtil {
    public static final String RESTBASEURL;
    public static final String RESTUSER;
    public static final String RESTPW;
    public static final String WORKSPACE;

    // 名字和其对应数组的位置
    public static Map<String, Integer> routeNameIndexMap = new HashMap<>();

    public static Map<String, Object> routeDict = null;

    public static List<GeoServerRESTManager> managerList = new ArrayList<>();
    public static List<GeoServerRESTReader> readerList = new ArrayList<>();
    public static List<GeoServerRESTPublisher> publisherList = new ArrayList<>();

    static {
        // 读取geoserver基本配置
        ResourceBundle resb = ResourceBundle.getBundle("GeoserverConfig");
//        RESTBASEURL = resb.getString("geoserver.distributedUrl");
        RESTBASEURL = resb.getString("geoserver.url");
        RESTUSER = resb.getString("geoserver.user");
        RESTPW = resb.getString("geoserver.password");
        WORKSPACE = resb.getString("geoserver.workspace");

        String routeDictString = resb.getString("geoserver.routeDict");
        ObjectMapper objectMapper = new ObjectMapper();


//        try {
//            // 转json
//            routeDict = objectMapper.readValue(routeDictString, Map.class);
//
//            // 生成好后存放到字典中
//            Integer index = 0;
//            for (Map.Entry<String, Object> entry : routeDict.entrySet()) {
//                String routeName = entry.getKey();
//                String routePort = (String) entry.getValue();
//
//                managerList.add(new GeoServerRESTManager(new URL(RESTBASEURL.replace("xxxx", routePort)), RESTUSER, RESTPW));
//                readerList.add(managerList.get(index).getReader());
//                publisherList.add(managerList.get(index).getPublisher());
//
//                routeNameIndexMap.put(routeName, index);
//                index += 1;
//            }
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
    }

    // 路由规则，以名字为字典
    private static String routeNameToPort(String imageName){
        String keyName = "else";

        for (Map.Entry<String, Integer> entry : routeNameIndexMap.entrySet()) {
            String routeName = entry.getKey();
            if(imageName.startsWith(routeName)){
                keyName = routeName;
            }
        }
        return keyName;
    }


    // 发布栅格图片
    // FileNotFoundException 是在publisher.publishGeoTIFF(WORKSPACE, storeName, img);时抛出的
    public static String publishTiff(String imagePath, String storeName) throws Exception {
        String keyName = routeNameToPort(storeName);
        // 根据keyName对应到端口号上
//        String port = (String) routeDict.get(keyName);
        String port = "8012";
        // 定位到对应的那个manger
        Integer index = routeNameIndexMap.get(keyName);


//        GeoServerRESTManager manager = managerList.get(index);
        GeoServerRESTManager manager = new GeoServerRESTManager(new URL(RESTBASEURL), RESTUSER, RESTPW);
//        GeoServerRESTReader reader = readerList.get(index);
        GeoServerRESTPublisher publisher = manager.getPublisher();

        // 判断工作区是否存在，如果不存在则创建工作区
        List<String> workspaces = manager.getReader().getWorkspaceNames();
        if(!workspaces.contains(WORKSPACE)){
            boolean wsres = publisher.createWorkspace(WORKSPACE);
            System.out.println("Creating workspace : " + WORKSPACE + " -> " + wsres);
        }
        System.out.println("Workspace initialed");

//        String storeName = "10_0";
//        String image_path = "/Users/jiangguanhua/Desktop/java_project2/MLTDS/target/classes/static/10_0.tif";
        // 如果不存在数据存储（dataStore）则创建数据存储
        System.out.println("---------------------");
        System.out.println(WORKSPACE + ":" + storeName);

        // TODO 目前dataStore访问的url地址不对，不能作为判断依据
        boolean publish = manager.getPublisher().publishGeoTIFF(WORKSPACE, storeName, new File(imagePath));
        System.out.println("TIFF文件发布，publish : " + publish);
        // 待修改
//        RESTDataStore dataStore = manager.getReader().getDatastore(WORKSPACE, storeName);
//        if(dataStore == null){
////            // geoserver-manager版本 > 1.8时可以使用
////            GSGeoTIFFDatastoreEncoder gsGeoTIFFDatastoreEncoder = new GSGeoTIFFDatastoreEncoder(storeName);
////            gsGeoTIFFDatastoreEncoder.setWorkspaceName(WORKSPACE);
////            gsGeoTIFFDatastoreEncoder.setUrl(new URL("file:" + imagePath));
////            boolean createStore = manager.getStoreManager().create(WORKSPACE, gsGeoTIFFDatastoreEncoder);
////            System.out.println("创建TIFF文件，create : " + createStore);
//            // TODO 对dataStore的判断无效，即使生成过也会再生成并覆盖之前的存储，待改进
//            boolean publish = manager.getPublisher().publishGeoTIFF(WORKSPACE, storeName, new File(imagePath));
//            System.out.println("TIFF文件发布，publish : " + publish);
//        } else {
//            System.out.println(String.format("已经存在%s", storeName));
//        }

//        manager.get
        return WORKSPACE;
    }
}
