package utils;

import java.io.*;

public class DictTools {

    static String dictPath="D:\\a我的文本校对\\TextAlter\\dictFile\\dict.txt";
    static String srcPath="D:\\a我的文本校对\\原文件";
    static String[] srcFileNames = new String[]{
            "sample_1_639", "sample_640_1400", "sample_1401_2000", "sample_2000_3300",
            "sample_3301_4300", "sample_4301_5000", "sample_5001_6400", "sample_6401_7120",
            "sample_7121_7200","sample_7201_8560", "sample_8561_9000", "sample_10001_20000",
            "sample_20001_40000", "sample_40001_50000", "sample_50001_60000","sample_80001_82000",
            "sample_82001_84000", "sample_84001_86000", "sample_86001_88000","sample_88001_90000",
            "sample_96001_98000", "sample_98001_100000"
    };
    static String resultPath="D:\\a我的文本校对\\合格文件";
    //获取文件目录下所有文件
    static File[] resultFiles=new File(resultPath).listFiles();
    //初始化文件IO流
    static BufferedReader br_src = null;
    static BufferedReader br_result = null;
    static BufferedWriter bw_dict = null;
    
    //分隔符
    static String CharSplit="=";
    //字符格式
    static String dict;
    public static void main(String[] args) {
        String num;
        for (File file:resultFiles
             ) {
            num=file.getName().split("_")[0];

//            br_src=new BufferedReader();
        }

    }




    public static void inputDict(File srcFile, File resultFile) throws IOException {
        br_src = new BufferedReader(new FileReader(srcFile));
        br_result = new BufferedReader(new FileReader(resultFile));
        bw_dict = new BufferedWriter(new FileWriter(dictPath, true));
        String src = "", result = "", wordOriginal = "", wordAlter = "";

        //同时有数据时
        while ((src = br_src.readLine()) != null && (result = br_result.readLine()) != null) {
            //合格文件没有修改的地方
            if (result.contains("O") || src.equals(result)) continue;

            //记录修改的文本
            if (result.contains("B-yinxiangsi")) {
                wordOriginal += src.charAt(0);
                wordAlter += result.charAt(0);
            }

            //记录修改的词组,进行存储到字典
            if (result.contains("I-yinxiangsi")) {
                wordOriginal += src.charAt(0);
                wordAlter += result.charAt(0);
                dict = wordOriginal + CharSplit + wordAlter;
                if (dict.length() != 5||!CharSplit.equals(String.valueOf(dict.charAt(2)))) continue;
                bw_dict.write(dict);//原来的字符=修改后的字符 就业=酒业
                bw_dict.newLine();
                bw_dict.flush();
                //重置
                wordOriginal="";
                wordAlter = "";
            }
        }
    }
}
