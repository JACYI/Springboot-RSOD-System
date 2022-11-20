package com.learning.mltds.utils.geoserver;

import Jama.Matrix;
import org.gdal.gdal.Dataset;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GeoUtils {
    private static final Double EARTH_RADIUS = 6378137.;//地球半径 单位是m
//
//    // 重复的代码抽出来
//    public GridCoverage2D getImage(String imagePath){
//        GridCoverage2D coverage = null;
//        try {
//            File file = new File(imagePath);
//            GeoTiffReader reader = new GeoTiffReader(file);
//            coverage = reader.read(null);
//
//            // 如果参考坐标系，需要切换一下坐标系，这里统一切换到EPSG:4326
//            CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem2D();
//            if (!crs.getName().toString().trim().equals("EPSG:WGS 84")) {
//                Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
//                CRSAuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", hints);
//                CoordinateReferenceSystem newCrs = factory.createCoordinateReferenceSystem("EPSG:4326");
//
//                coverage = (GridCoverage2D) Operations.DEFAULT.resample(coverage, newCrs);
//
//            }
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return coverage;
//    }
//
//    // 获取图片的信息以及地理信息
//    public Map<String, Object> getImageInfo(String imagePath){
//        Map<String, Object> imageInfoMap = new HashMap<>();
//        try{
//            File file = new File(imagePath);
////            GeoTiffReader reader=new GeoTiffReader(file);
////            GridCoverage2D coverage = reader.read(null);
////
////            // 如果参考坐标系，需要切换一下坐标系，这里统一切换到EPSG:4326
////            CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem2D();
////            if(!crs.getName().toString().trim().equals("EPSG:WGS 84")){
////                Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
////                CRSAuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", hints);
////                CoordinateReferenceSystem newCrs = factory.createCoordinateReferenceSystem("EPSG:4326");
////
////                coverage = (GridCoverage2D) Operations.DEFAULT.resample(coverage, newCrs);;
////            }
//
//            GridCoverage2D coverage = getImage(imagePath);
//            Envelope2D coverageEnvelope = coverage.getEnvelope2D();
//
//            // 获取图片信息
//            RenderedImage image = coverage.getRenderedImage();
//            Integer imageWidth = image.getWidth();
//            Integer imageHeight = image.getHeight();
//
////            Integer imageBands = image.getData().getNumBands();
//            // TODO
//            Integer imageBands = 3;
//
//            // 获取图片的经纬度信息
//            Double longitude = coverageEnvelope.getBounds2D().getMinX();
//            Double latitude = coverageEnvelope.getBounds2D().getMinY();
//            Double maxLongitude = coverageEnvelope.getBounds2D().getMaxX();
//            Double maxLatitude = coverageEnvelope.getBounds2D().getMaxY();
//
//            List<Double> geoBounds = new ArrayList<Double>(){{add(longitude);add(latitude);add(maxLongitude);add(maxLatitude);}};
//
//            List<Double> geoCenter = new ArrayList<Double>(){{add((longitude + maxLongitude) / 2.);add((latitude + maxLatitude) / 2.);}};
//
//            // 计算图像分辨率
//            Double spatialResolution = getResolution(latitude, longitude, maxLatitude, maxLongitude, imageWidth, imageHeight);
//
//            // 冗余信息太多，待优化
//            imageInfoMap.put("filename", file.getName());
//            imageInfoMap.put("longitude", longitude);
//            imageInfoMap.put("latitude", latitude);
//            imageInfoMap.put("image_width", imageWidth);
//            imageInfoMap.put("image_height", imageHeight);
//            imageInfoMap.put("spatial_resolution", spatialResolution);
//            imageInfoMap.put("image_bands", imageBands);
//            imageInfoMap.put("sat_type", null);
//            imageInfoMap.put("image_type", null);
//            imageInfoMap.put("sensor_type", null);
//            imageInfoMap.put("path", imagePath);
//            imageInfoMap.put("observation_time", null);
//            imageInfoMap.put("geo_center", geoCenter);
//            imageInfoMap.put("extent", geoBounds);
//            imageInfoMap.put("product_time", null);
//            imageInfoMap.put("projected_coordinate_x", longitude);
//            imageInfoMap.put("projected_coordinate_y", latitude);
//
//            // TODO 解析图片名称，填充sat_type, sensor_type以及image_type
//
//            //TODO 解析meta.xml文件
//
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//        return imageInfoMap;
//    }
//
//
    // TODO 经纬度转图片坐标，这个做出来对于有roate角的不准，待修改
    public static List<Integer> lonLat2Bbox(TiffDataset dataset, List<Double> geoBbox){
        List<Integer> bbox = new ArrayList<>();

        for(int i=0; i< geoBbox.size()/2; i++) {
            double lon = geoBbox.get(i * 2), lat = geoBbox.get(i * 2 + 1);
            double[] geoCoord = lonLat2Geo(dataset.getDataset(), lon, lat);
            Integer[] imageCoord = geo2imagexy(dataset.getDataset(), geoCoord[0], geoCoord[1]);

            bbox.addAll(Arrays.asList(imageCoord));
        }

//        GridCoverage2D coverage = getImage(imagePath);
//        Envelope2D coverageEnvelope = coverage.getEnvelope2D();
//        RenderedImage image = coverage.getRenderedImage();
//        List<Double> list1 = MillierConvertion(coverageEnvelope.getBounds2D().getMinX(), coverageEnvelope.getBounds2D().getMinY());
//        List<Double> list2 = MillierConvertion(coverageEnvelope.getBounds2D().getMinX(), coverageEnvelope.getBounds2D().getMaxY());
//        List<Double> list3 = MillierConvertion(coverageEnvelope.getBounds2D().getMaxX(), coverageEnvelope.getBounds2D().getMinY());
//        // 将投影坐标转换为图片坐标
//        Double projWidth = Math.abs(list3.get(0) - list1.get(0));
//        Double projHeight = Math.abs(list2.get(1) - list1.get(1));
//        Double projWidthRes = image.getWidth() /  projWidth.doubleValue();
//        Double projHeightRes = image.getHeight() /  projHeight.doubleValue();
//
//        List<Double> projMinXY = MillierConvertion(coverageEnvelope.getBounds2D().getMinX(), coverageEnvelope.getBounds2D().getMinY());
//
//        List<Integer> bbox = new ArrayList<>();
//        for(int i=0; i<geoBbox.size(); i+=2){
//            Double longitude = geoBbox.get(i);
//            Double latitude = geoBbox.get(i + 1);
//            List<Double> coord = MillierConvertion(longitude, latitude);
//            Double width = Math.abs(coord.get(0) - projMinXY.get(0)) * projWidthRes;
//            Double height = image.getHeight() -  Math.abs(coord.get(1) - projMinXY.get(1)) * projHeightRes;
//
//            bbox.add(width.intValue());
//            bbox.add(height.intValue());
//        }
        return bbox;
    }

    // 图片坐标转经纬度
    public static double[] geo2LonLat(Dataset dataset, double x, double y) {
        SpatialReference prosrs = new SpatialReference();
        prosrs.ImportFromWkt(dataset.GetProjectionRef());
        SpatialReference geosrs = prosrs.CloneGeogCS();

        CoordinateTransformation ct = CoordinateTransformation.CreateCoordinateTransformation(prosrs, geosrs);
        return ct.TransformPoint(x, y);
    }


    /**
     * 通过经纬度坐标获取矩形框的长度和宽度(米)
     * @param geoBbox 经纬度坐标 [p1_lon, p1_lat, p2_lon, p2_lat, ... ]
     * @return  Map: {"length": , "width": }
     */
    public static Map<String, Double> getGeoRectSize(List<Double> geoBbox) {
        double[] a = new double[]{geoBbox.get(0), geoBbox.get(1)};
        double[] b = new double[]{geoBbox.get(2), geoBbox.get(3)};
        double[] c = new double[]{geoBbox.get(4), geoBbox.get(5)};

        double a1 = distance(a, b);
        double b1 = distance(b, c);
//        List<List<Double>> res1 = new ArrayList<>();
//        List<List<Double>> res2 = new ArrayList<>();
//        res1.add(new ArrayList<>(Arrays.asList(a[0], a[1])));
//        res1.add(new ArrayList<>(Arrays.asList(b[0], b[1])));
//        res2.add(new ArrayList<>(Arrays.asList(b[0], b[1])));
//        res2.add(new ArrayList<>(Arrays.asList(c[0], c[1])));

//        HashMap<String, List<List<Double>>> lengthAndWidth = new HashMap<>();
        HashMap<String, Double> lengthAndWidth = new HashMap<>();
        if(a1 > b1){
            lengthAndWidth.put("length", a1);
            lengthAndWidth.put("width", b1);
        } else {
            lengthAndWidth.put("length", b1);
            lengthAndWidth.put("width", a1);
        }
        return lengthAndWidth;
    }


    /**
     * 将经纬度坐标转为投影坐标（具体的投影坐标系由给定数据确定）
     * @param dataset   GDAL地理数据
     * @param lon       地理坐标lon经度
     * @param lat       地理坐标lat纬度
     * @return          经纬度坐标(lon, lat)对应的投影坐标(projx, projy)
     */
    public static double[] lonLat2Geo(Dataset dataset, double lon, double lat){
        SpatialReference prosrs = new SpatialReference(), geosrs = null;
        prosrs.ImportFromWkt(dataset.GetProjectionRef());    // 投影参考系
        geosrs = prosrs.CloneGeogCS();                       // 地理参考系
        // 创建坐标映射对象
        CoordinateTransformation ct = CoordinateTransformation.CreateCoordinateTransformation(geosrs, prosrs);
        return ct.TransformPoint(lon, lat);
    }

    /**
     * 获得给定数据的投影参考系和地理参考系
     *
     * @param dataset   GDAL地理数据
     * @return          投影参考系和地理参考系
     */
    public static void getSRSPair(Dataset dataset, SpatialReference prosrs, SpatialReference geosrs){
        prosrs.ImportFromWkt(dataset.GetProjectionRef());    // 投影参考系
        geosrs = prosrs.CloneGeogCS();                       // 地理参考系
    }

    /**
     * 根据GDAL的六参数模型将给定的投影或地理坐标转为影像图像坐标(x, y)
     *
     * @param dataset   GDAL地理数据
     * @param px        投影projx或地理坐标lon
     * @param py        投影projy或地理坐标lat
     * @return          投影坐标或经纬度坐标对应的影像图像坐标(x, y)
     */
    public static Integer[] geo2imagexy(Dataset dataset, double px, double py){
        double[] trans = dataset.GetGeoTransform();

//        List<Double> oneTwo = new ArrayList<>(Arrays.asList());
//        List<Double> fourFive = new ArrayList<>(Arrays.asList());
//        List<List<Double>> a = new ArrayList<>(Arrays.asList(oneTwo, fourFive));  // y坐标是正值

//        List<Double> b = new ArrayList<>(Arrays.asList(px - trans[0], py - trans[3]));
        // 使用JAMA库的最小二乘法解线性方程组 a * x = b
        Matrix a = new Matrix(new double[][]{{trans[1], trans[2]}, {trans[4], trans[5]}});
        Matrix b = new Matrix(new double[][]{{px - trans[0]}, {py - trans[3]}});
        Matrix x = a.solve(b);  // 2 * 1
        // 返回列（2）
        double[] resDouble = x.getRowPackedCopy();
        // 转为整数
        Integer[] resInt = new Integer[resDouble.length];
        for(int i=0; i<resDouble.length; i++)
            resInt[i] = (int) Math.round(resDouble[i]);
        return resInt;
    }

    /**
     * 用来计算两点之间实际距离
     * @param point1    点1经纬度坐标数组 [longitude, latitude]
     * @param point2    点2经纬度坐标数组
     * @return          点1和点2的实际距离（米）
     */
    private static long distance(double[] point1, double[] point2) {
        double lat1 = point1[1] * Math.PI / 180.0;
        double lon1 = point1[0] * Math.PI / 180.0;
        double lat2 = point2[1] * Math.PI / 180.0;
        double lon2 = point2[0] * Math.PI / 180.0;

        double vlon = Math.abs(lon2 - lon1);
        double vlat = Math.abs(lat2 - lat1);

        double h = Haversin(vlat) + Math.cos(lat1) * Math.cos(lat2) * Haversin(vlon);
        double length = 2 * 6371008.8 * Math.sin(Math.sqrt(h));

        return Math.round(length);
    }

    private static double Haversin(double theta){
        double v = Math.sin(theta / 2);
        return v * v;
    }
//
//    @Test
//    public  void test(){
//        String imagePath = "/Users/jiangguanhua/Desktop/java_project2/MLTDS/src/main/resources/static/09APR28160451-P2AS-014012660170_01_P001.TIF";
////        String imagePath = "/Users/jiangguanhua/Desktop/java_project2/MLTDS/src/main/resources/static/10_0.tif";
//        File file = new File(imagePath);
//        try {
//            GeoTiffReader reader=new GeoTiffReader(new File(imagePath));
//            reader.getMetadata();
//            GridCoverage2D coverage = reader.read(null);
//            CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem2D();
//            System.out.println(crs.getName());
//
//
//
////            System.out.println(crs);
//
//
//
//
//
////            double coverageMinX = coverageEnvelope.getBounds().getMinX();
////            double coverageMaxX = coverageEnvelope.getBounds().getMaxX();
////            double coverageMinY = coverageEnvelope.getBounds().getCenterY();
////            double coverageMaxY = coverageEnvelope.getBounds().getCenterX();
////            System.out.println(coverageEnvelope.getBounds());
////            System.out.println(coverageMinY);
////            System.out.println(coverageMaxY);
//
////            Reader br = new Reader();
////            GridCoverage2D old2D = br.getGridCoverage2D(file);
//
//
////            final CoordinateReferenceSystem WGS = CRS.decode("EPSG:4326");
////            final CoordinateReferenceSystem sourceCRS = coverage.getCoordinateReferenceSystem();
////
////            Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
////            CRSAuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", hints);
////            CoordinateReferenceSystem newcrs = factory.createCoordinateReferenceSystem("EPSG:4326");
////            Operations.DEFAULT.resample(coverage,newcrs);
////
////            System.out.println(String.format("源坐标系为: %s", sourceCRS.getName()));
////
////            GridCoverage2D new2D = (GridCoverage2D) Operations.DEFAULT.resample(coverage, WGS);
////            System.out.println(new2D.getEnvelope2D().getBounds2D());
////            System.err.println(String.format("目标坐标系为: %s", new2D.getCoordinateReferenceSystem().getName()));
//
//
//
////            final CoordinateReferenceSystem WGS = CRS.decode("EPSG:4326");
////            final CoordinateReferenceSystem sourceCRS = coverage.getCoordinateReferenceSystem();
//
//
//            // 坐标系切换，转EPSG:4326
//            // https://blog.csdn.net/zhanggqianglovec/article/details/104166124
//
////            System.out.println(crs.getName());
////            if(!crs.getName().toString().trim().equals("EPSG:WGS 84")){
////                Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
////                CRSAuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", hints);
////                CoordinateReferenceSystem newCrs = factory.createCoordinateReferenceSystem("EPSG:4326");
////
////                coverage = (GridCoverage2D) Operations.DEFAULT.resample(coverage, newCrs);;
////            }
////
////
////
////
////            Envelope2D coverageEnvelope = coverage.getEnvelope2D();
////
////            System.out.println(coverageEnvelope.getCoordinateReferenceSystem());
////            System.out.println(coverageEnvelope.getBounds2D());
////            System.out.println(coverageEnvelope.getBounds2D().getMinX());
////            System.out.println(coverageEnvelope.getBounds2D().getMaxX());
//
//
//
//
//
//
//
////            RenderedImage image = coverage.getRenderedImage();
////
////            System.out.println(image.getWidth());
////            System.out.println(image.getHeight());
////            Object[] arr = new Object[3];
////            for(Object b:arr){
////                System.out.println(b);
////            }
////            int []rgbData = new  int[100 * 100];
////            image.getData().getPixels(3000, 3000, 100, 100, rgbData);
//
////            int[][] rgbArray = new int[100][100];
////
////            for(int i=0; i<100*100 ; i++){
////                rgbArray[i / 100][i%100] = rgbData[i];
////            }
////
////            String path = "/Users/jiangguanhua/Desktop/java_project2/MLTDS/src/main/resources/static/0.jpg";
////            writeImageFromArray(path, "jpg", rgbArray);
//
//
//
//
//
//
////            System.out.println(image.getHeight());
////            System.out.println(image.getWidth());
////
////            System.out.println(image.getData().getNumBands());
////
////
////
////            List<Double> geoBbox = new ArrayList<Double>();
////            geoBbox.add(-76.32763010627339);
////            geoBbox.add(36.95556988113587);
////            geoBbox.add(-76.32781946853599);
////            geoBbox.add(36.95478767155156);
////            geoBbox.add(-76.33167769479716);
////            geoBbox.add(36.95535546644194);
////            geoBbox.add(-76.33142331126696);
////            geoBbox.add(36.95616334351266);
////
////
////            List<Double> list1 = MillierConvertion(coverageEnvelope.getBounds2D().getMinX(), coverageEnvelope.getBounds2D().getMinY());
////            List<Double> list2 = MillierConvertion(coverageEnvelope.getBounds2D().getMinX(), coverageEnvelope.getBounds2D().getMaxY());
////            List<Double> list3 = MillierConvertion(coverageEnvelope.getBounds2D().getMaxX(), coverageEnvelope.getBounds2D().getMinY());
////            List<Double> list4 = MillierConvertion(coverageEnvelope.getBounds2D().getMaxX(), coverageEnvelope.getBounds2D().getMaxY());
////
////
////            Double projWidth = Math.abs(list3.get(0) - list1.get(0));
////            Double projHeight = Math.abs(list2.get(1) - list1.get(1));
////            System.out.println(projWidth);
////            System.out.println(projHeight);
////            Double projWidthRes = image.getWidth() /  projWidth.doubleValue();
////            Double projHeightRes = image.getHeight() /  projHeight.doubleValue();
////
////
////            List<Double> projMinXY = MillierConvertion(coverageEnvelope.getBounds2D().getMinX(), coverageEnvelope.getBounds2D().getMinY());
////
////            System.out.println("---------------");
////            for(int i=0; i<geoBbox.size(); i+=2){
////                Double longitude = geoBbox.get(i);
////                Double latitude = geoBbox.get(i + 1);
////                List<Double> coord = MillierConvertion(longitude, latitude);
////                Double width = Math.abs(coord.get(0) - projMinXY.get(0)) * projWidthRes;
////                Double height = image.getHeight() -  Math.abs(coord.get(1) - projMinXY.get(1)) * projHeightRes;
////                System.out.println(width);
////                System.out.println(height);
////
////            }
//
//
////            String readFormats[] = ImageIO.getReaderFormatNames();
////            String writeFormats[] = ImageIO.getWriterFormatNames();
////            System.out.println("Readers: " + Arrays.asList(readFormats));
////            System.out.println("Writers: " + Arrays.asList(writeFormats));
//
//
//
//            imageCut(14000, 14000, 4000, 4000, "/Users/jiangguanhua/Desktop/java_project2/MLTDS/src/main/resources/static/10_0.tif", "/Users/jiangguanhua/Desktop/java_project2/MLTDS/src/main/resources/static/ttt.jpg");
//
////            File f = new File("/Users/jiangguanhua/Desktop/java_project2/MLTDS/src/main/resources/static/10_0.tif");
////            BufferedImage bi = ImageIO.read(f);
////            System.out.println();
//
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 图片剪裁
//     *
//     * @param x          距离左上角的x轴距离
//     * @param y          距离左上角的y轴距离
//     * @param width      宽度
//     * @param height     高度
//     * @param sourcePath 图片源
//     * @param descpath   目标位置
//     */
//    public  void imageCut(int x, int y, int width, int height,
//                          String sourcePath, String descpath) {
//        FileInputStream fis = null;
//        ImageInputStream iis = null;
//        try {
//
//            String fileSuffix = sourcePath.substring(sourcePath.lastIndexOf(".") + 1);
//
//            String dstFileSuffix = descpath.substring(descpath.lastIndexOf(".") + 1);
//            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(fileSuffix);
//            ImageReader reader = it.next();
//
//            fis = new FileInputStream(sourcePath);
//            iis = ImageIO.createImageInputStream(fis);
//
//            reader.setInput(iis, true);
//
//            ImageReadParam param = reader.getDefaultReadParam();
//            Integer imHeight = reader.getHeight(0);
//            Integer imWidth =  reader.getWidth(0);
//
//            x = Math.max(x, 0);
//            y = Math.max(y, 0);
//            width = Math.min(width, imWidth - x);
//            height = Math.min(height, imHeight - y);
//
//            Rectangle rect = new Rectangle(x, y, width, height);
//            param.setSourceRegion(rect);
//
//            BufferedImage bi = reader.read(0, param);
//            ImageIO.write(bi, dstFileSuffix, new File(descpath));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (fis != null) {
//                try {
//                    fis.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                fis = null;
//            }
//            if (iis != null) {
//                try {
//                    iis.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                iis = null;
//            }
//        }
//    }
//
//
//    public  void writeImageFromArray(String imageFile, String type, int[][] rgbArray)
//            throws FileNotFoundException {
//        // 获取数组宽度和高度
//        int width = rgbArray[0].length;
//        int height = rgbArray.length;
//        System.out.println("width = " + width + " height = " + height);
//
//        int[] data = new int[width * height];
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                data[i * width + j] = rgbArray[i][j];
//                data[i * width + j] = data[i * width + j];
//            }
//        }
//
//        // 将数据写入BufferedImage
//        BufferedImage bf = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//        bf.setRGB(0, 0, width, height, data, 0, width);
//        // 输出图片
//        try {
//            File file = new File(imageFile);
//            ImageIO.write((RenderedImage) bf, type, file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public List<Double>  MillierConvertion2( double lon, double lat)
////    public List<Double>  MillierConvertion( double lon, double lat)
//    {
//        Double L = 6381372 * Math.PI * 2;//地球周长
//        Double W = L;// 平面展开后，x轴等于周长
//        Double H = L / 2;// y轴约等于周长一半
//        Double mill = 2.3;// 米勒投影中的一个常数，范围大约在正负2.3之间
//        Double x = lon * Math.PI / 180;// 将经度从度数转换为弧度
//        Double y = lat * Math.PI / 180;// 将纬度从度数转换为弧度
//        y = 1.25 * Math.log(Math.tan(0.25 * Math.PI + 0.4 * y));// 米勒投影的转换
//        // 弧度转为实际距离
//        x = (W / 2) + (W / (2 * Math.PI)) * x;
//        y = (H / 2) - (H / (2 * mill)) * y;
//        List<Double> result = new ArrayList<Double>();
//        result.add(x);
//        result.add(y);
//
//        return result;
//    }
//
//    //https://blog.csdn.net/theonegis/article/details/50259705?_t=t
//    public static List<Double> MillierConvertion(double lon, double lat) {
////    public static double[] convert(double lon, double lat) {
//        // 传入原始的经纬度坐标
//        Point targetPoint = null;
//        try{
//            Coordinate sourceCoord = new Coordinate(lon, lat);
//            GeometryFactory geoFactory = JTSFactoryFinder.getGeometryFactory( null );
//            Point sourcePoint = geoFactory.createPoint(sourceCoord);
//
//            // 这里是以OGC WKT形式定义的是World Mercator投影，网页地图一般使用该投影
//            final String strWKTMercator = "PROJCS[\"World_Mercator\","
//                    + "GEOGCS[\"GCS_WGS_1984\","
//                    + "DATUM[\"WGS_1984\","
//                    + "SPHEROID[\"WGS_1984\",6378137,298.257223563]],"
//                    + "PRIMEM[\"Greenwich\",0],"
//                    + "UNIT[\"Degree\",0.017453292519943295]],"
//                    + "PROJECTION[\"Mercator_1SP\"],"
//                    + "PARAMETER[\"False_Easting\",0],"
//                    + "PARAMETER[\"False_Northing\",0],"
//                    + "PARAMETER[\"Central_Meridian\",0],"
//                    + "PARAMETER[\"latitude_of_origin\",0],"
//                    + "UNIT[\"Meter\",1]]";
//            CoordinateReferenceSystem mercatroCRS = CRS.parseWKT(strWKTMercator);
//            // 做投影转换，将WCG84坐标转换成世界墨卡托投影转
//            MathTransform transform = CRS.findMathTransform(DefaultGeographicCRS.WGS84, mercatroCRS);
//            targetPoint = (Point) JTS.transform(sourcePoint, transform);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//
//
//        List<Double> result = new ArrayList<>();
//        result.add(targetPoint.getX());
//        result.add(targetPoint.getY());
//        return result;
//    }
//
//
//
//
//
//    // 根据经纬度和图片大小计算图像分辨率，单位m
//    private Double getResolution(Double minLat, Double minLng, Double maxLat, Double maxLng, Integer imageWidth, Integer imageHeight){
//
//        // 转经纬度距离为地理距离(m)
//        Double RasterXDistance = getDistance(minLat, minLng, minLat, maxLng);
//        Double RasterYDistance = getDistance(minLat, minLng, maxLat, minLng);
//
//        return (RasterXDistance / imageWidth + RasterYDistance / imageHeight) / 2.;
//
//    }
//
//    // 获取两点之间的距离，有经纬度距离转为米
//    private Double getDistance(Double lat1, Double lng1, Double lat2, Double lng2){
//        Double radLat1 = rad(lat1);
//        Double radLng1 = rad(lng1);
//
//        Double radLat2 = rad(lat2);
//        Double radLng2 = rad(lng2);
//
//        Double a = radLat1 - radLat2;
//        Double b = radLng1 - radLng2;
//
//
//        Double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
//        s = s * this.EARTH_RADIUS;
//
//        return s;
//    }
//
//    private Double rad(Double d){
//        return d * Math.PI / 180.0;
//    }
}
