package utils;

import java.io.FileReader;
import java.util.Properties;

/**
 * @author Mr.Xu
 * @version 1.0
 * @description: TODO
 * @date 2021/5/21 11:16
 */
public class MyTool {
    private static Properties ResultFileTxt;

    //获取文件路径
    static String orginPath=ResultFileTxt.getProperty("orginPath");
    static String alterPath=ResultFileTxt.getProperty("alterPath");
    static String resultPath=ResultFileTxt.getProperty("resultPath");

    public static boolean doFileExtis(){
        for (String file:new String[]{orginPath,alterPath,resultPath}
             ) {

        }
        return false;
    }


    /**
     * @description: 加载配置文件
     * @param: 
     * @return: 
     * @date: 2021/5/21 11:18
     */ 
    MyTool(){
        //创建资源文件对象 获取加载字典文件
        ResultFileTxt = new Properties();
        try {
            //获取文件中所有的键值对，存放进行ResultFileTxt对象
            ResultFileTxt.load(new FileReader("setting.properties"));
        } catch (Exception e) {
            System.out.println("文件不存在");
        }


    }
}
