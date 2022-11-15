package com.learning.mltds;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.ogr.ogr;
import org.gdal.osr.SpatialReference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Enumeration;
import java.util.Vector;

@SpringBootTest
class MltdsApplicationTests {

    @Test
    void contextLoads() {
        // gdal工具初始化
        gdal.AllRegister();


//        String filePath = "C:\\Users\\yiyonghao\\SpringbootProject\\bs\\backend\\images\\source\\Google-1m_1_1.tiff";
        String filePath = "C:\\Users\\yiyonghao\\SpringbootProject\\bs\\backend\\images\\source\\15JUN11171455-P2AS-014012660080_01_P001.TIF";
        /* -------------------------------------------------------------------- */
        /*      Report general info.                                            */
        /* -------------------------------------------------------------------- */
        Dataset hDataset = gdal.Open(filePath);
        if (hDataset == null) {
            System.err
                    .println("GDALOpen failed - " + gdal.GetLastErrorNo());
            System.err.println(gdal.GetLastErrorMsg());
            System.exit(1);
        }

        // Report general info.
        Driver hDriver = hDataset.GetDriver();
        System.out.println("Driver: " + hDriver.getShortName() + "/"
                + hDriver.getLongName());
        Vector papszFileList = hDataset.GetFileList( );
        if( papszFileList.size() == 0 ) {
            System.out.println( "Files: none associated" );
        } else {
            Enumeration e = papszFileList.elements();
            System.out.println( "Files: " + (String)e.nextElement() );
            while(e.hasMoreElements())
                System.out.println( "       " +  (String)e.nextElement() );
        }
        System.out.println("Size is " + hDataset.getRasterXSize() + ", "
                + hDataset.getRasterYSize());
        System.out.println("Count is " + hDataset.getRasterCount());

        /* -------------------------------------------------------------------- */
        /*      Report projection.                                              */
        /* -------------------------------------------------------------------- */
//        System.out.println(hDataset.GetProjection());
        if (hDataset.GetProjectionRef() != null) {
            SpatialReference hSRS;
            String pszProjection;

            pszProjection = hDataset.GetProjectionRef();

            hSRS = new SpatialReference(pszProjection);
            if (hSRS != null && pszProjection.length() != 0) {
                String[] pszPrettyWkt = new String[1];

                hSRS.ExportToPrettyWkt(pszPrettyWkt, 0);
                System.out.println("Coordinate System is:");
                System.out.println(pszPrettyWkt[0]);
                //gdal.CPLFree( pszPrettyWkt );
            } else
                System.out.println("Coordinate System is `"
                        + hDataset.GetProjectionRef() + "'");

            hSRS.delete();
        }

        /* -------------------------------------------------------------------- */
        /*      Report Geotransform.                                            */
        /* -------------------------------------------------------------------- */
        double[] adfGeoTransform = new double[6];
        hDataset.GetGeoTransform(adfGeoTransform);
        {
            if (adfGeoTransform[2] == 0.0 && adfGeoTransform[4] == 0.0) {
                System.out.println("Origin = (" + adfGeoTransform[0] + ","
                        + adfGeoTransform[3] + ")");

                System.out.println("Pixel Size = (" + adfGeoTransform[1]
                        + "," + adfGeoTransform[5] + ")");
            } else {
                System.out.println("GeoTransform =");
                System.out.println("  " + adfGeoTransform[0] + ", "
                        + adfGeoTransform[1] + ", " + adfGeoTransform[2]);
                System.out.println("  " + adfGeoTransform[3] + ", "
                        + adfGeoTransform[4] + ", " + adfGeoTransform[5]);
            }
        }
    }



}
