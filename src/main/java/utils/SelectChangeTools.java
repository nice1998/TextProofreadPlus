package utils;

import java.io.*;
import java.util.Properties;

public class SelectChangeTools {
    //初始化IO
    static BufferedReader br1 = null;
    static BufferedReader br2 = null;
    static BufferedWriter bw = null;
    static BufferedReader br = null;
    static FileReader fr = null;
    static FileWriter fw = null;
    static String srcFilePath = "D:\\a我的文本校对\\原文件\\";
    //sample_1_639
    static String resultFilePath = "D:\\a我的文本校对\\合格文件\\合格\\";
    //
    static String dictPath = "D:\\a我的文本校对\\TextAlter\\dictFile\\dictNone.txt";
    static String dictWordPath = "D:\\a我的文本校对\\TextAlter\\dictFile\\dictWord.txt";
    static String[] srcFileNames = new String[]{
            "sample_1_639", "sample_640_1400", "sample_1401_2000", "sample_2000_3300",
            "sample_3301_4300", "sample_4301_5000", "sample_5001_6400", "sample_6401_7120",
            "sample_7121_7200"
    };


    static int a = "结 O识 O=解 B-yinxiangsi释 I-yinxiangsi".length();
    static String re, CharSplit = "=";
    static File dictWord = new File(dictWordPath);
    static File dict = new File(dictPath);


    static int countWords = 0;//记录修改次数
    static int beforeWordChangeStatus = 0;//记录前一个词修改状态


    public static final String status_O = " O";
    public static final String status_B = " B-yinxiangsi";
    public static final String status_I = " I-yinxiangsi";

    public static void main(String[] args) {


        String srcFileName;//序号.txt  当前原文件名
        String resultFileName;//序号.txt  当前修改后文件名
        try {

            /*
            //  3进行分词  将无格式文本进行分句,记录修改次数
            String word;//分词后的单个或词组

                words = WordSegmenter.segWithStopWords(noneAll);//句子进行分词*/
//                System.out.println("未标注词性：" + words);
            //词性标注
//                PartOfSpeechTagging.process(words);
//                System.out.println("标注词性：" + words);

        } finally {
            CloseAllStream();
        }

    }

    /**
     * 将修改过的词组添加到字典 格式:原来的字符=修改后的字符 就业=酒业
     *
     * @param srcFile    原文件
     * @param resultFile 修改后文件
     * @throws IOException
     */
    public static void selectFile(File srcFile, File resultFile) throws IOException {
        br1 = new BufferedReader(new FileReader(srcFile));
        br2 = new BufferedReader(new FileReader(resultFile));
        bw = new BufferedWriter(new FileWriter(dict, true));
        String s1 = "", s2 = "", wordOriginal = "", wordAlter = "";

        //同时有数据时
        while ((s1 = br1.readLine()) != null && (s2 = br2.readLine()) != null) {
            //合格文件没有修改的地方
            if (s2.contains("O") || s1.equals(s2)) continue;

            //记录修改的文本
            if (s2.contains("B-yinxiangsi")) {
                wordOriginal = s1.split(" ")[0];
                wordAlter = s2.split(" ")[0];
            }

            //记录修改的词组,进行存储到字典
            if (s2.contains("I-yinxiangsi")) {
                wordOriginal += s1.split(" ")[0];
                wordAlter += s2.split(" ")[0];
                bw.write(wordOriginal + "=" + wordAlter);//原来的字符=修改后的字符 就业=酒业
                bw.newLine();
                bw.flush();
                wordAlter = "";
            }
        }
    }


    /**
     * 将修改过的单个文字添加到字典 格式:原来的字符=修改后的字符 的=得
     *
     * @param srcFile    原文件
     * @param resultFile 修改后文件
     * @throws IOException
     */
    public static void selectFile3(File srcFile, File resultFile) throws IOException {
        br1 = new BufferedReader(new FileReader(srcFile));
        br2 = new BufferedReader(new FileReader(resultFile));
        bw = new BufferedWriter(new FileWriter(dictWord, true));
        String s1 = "", s2 = "", wordOriginal = null, wordAlter = null;

        //同时有数据时
        while ((s1 = br1.readLine()) != null && (s2 = br2.readLine()) != null) {
            //合格文件没有修改的地方

            if (wordAlter != null && wordOriginal != null) {
                if (!s2.contains("I-yinxiangsi") && wordAlter.contains("B-yinxiangsi")) {
                    bw.write(wordOriginal.charAt(0) + "=" + wordAlter.charAt(0));
                    bw.newLine();
                    bw.flush();
                    wordOriginal=null;
                    wordAlter=null;
                }
            }
            //记录修改的文本
            if (s2.contains("B-yinxiangsi")) {
                if (!String.valueOf(s1.charAt(0)).equals(String.valueOf(s2.charAt(0)))){
                wordOriginal = s1;
                wordAlter = s2;
                }
            }

        }
    }

    /**
     * 将修改过的词组添加到字典 格式:原来的字符=修改后的字符 就业=酒业
     *
     * @param srcFile    原文件
     * @param resultFile 修改后文件
     * @throws IOException
     */
    public static void selectFile2(File srcFile, File resultFile) throws IOException {
        br1 = new BufferedReader(new FileReader(srcFile));
        br2 = new BufferedReader(new FileReader(resultFile));
        bw = new BufferedWriter(new FileWriter(dict, true));
        String s1 = "", s2 = "", wordOriginal = "", wordAlter = "";

        //同时有数据时
        while ((s1 = br1.readLine()) != null && (s2 = br2.readLine()) != null) {
            //合格文件没有修改的地方
            if (s2.contains("O") || s1.equals(s2)) continue;

            //记录修改的文本
            if (s2.contains("B-yinxiangsi")) {
                wordOriginal = s1;
                wordAlter = s2;
            }

            //记录修改的词组,进行存储到字典
            if (s2.contains("I-yinxiangsi")) {
                wordOriginal += s1;
                wordAlter += s2;
                re = wordOriginal + CharSplit + wordAlter;
                if (re.length() != a) continue;
                bw.write(re);//原来的字符=修改后的字符 就业=酒业
                bw.newLine();
                bw.flush();
                wordAlter = "";
            }
        }
    }


    /**
     * 读取文件,将文件变成无格式文本
     *
     * @param filePath   源文件地址
     * @param fileTarget 无格式文件地址
     * @throws IOException
     */
    public static void ChangeFileToPlain(String filePath, String fileTarget) throws IOException {
        File file = new File(filePath);
        String readLine = "";
        br = new BufferedReader(new FileReader(file));
        bw = new BufferedWriter(new FileWriter(fileTarget));
        while ((readLine = br.readLine()) != null) {
            //去掉格式
            readLine = readLine.replaceAll(" O", "");
            bw.write(readLine);
            bw.flush();
        }
    }

    /**
     * 读取文件,将格式文本文件变成原文本
     *
     * @param filePath   源文件地址
     * @param fileTarget 无格式文件地址
     * @throws IOException
     */
    public static void ReChangeFileToOriginal(String filePath, String fileTarget) throws IOException {
        String readLine = "";
        fr = new FileReader(new File(filePath));
        fw = new FileWriter(new File(fileTarget));
        char[] b = new char[1];
        int len = 0;
        while ((len = fr.read(b)) != -1) {
            fw.write(b, 0, len);
            fw.write(" O\n");//原来的格式
            fw.flush();
        }
    }

    /**
     * 将无格式字典转换为有格式字典
     *
     * @throws IOException
     */
    public static void ChangeDictionary() throws IOException {
        br1 = new BufferedReader(new FileReader(new File("D:\\a我的文本校对\\TextAlter\\dictFile\\dictNone.txt")));
        bw = new BufferedWriter(new FileWriter(new File("D:\\a我的文本校对\\TextAlter\\dictFile\\dict.txt")));
        String dict, styleDict;

        while ((dict = br1.readLine()) != null) {
            if (dict.length() != 5) {
                System.out.println(dict);
            } else {
                styleDict = dict.charAt(0) + status_O + dict.charAt(1) + status_O + dict.charAt(2) + dict.charAt(3) + status_B + dict.charAt(4) + status_I;
                bw.write(styleDict);
                bw.newLine();
                bw.flush();
            }
        }
    }

    /**
     * 获取字典键值对的值
     *
     * @param word
     * @param dictPath
     * @return
     */
    public static String getDictWord(String word, String dictPath) {
        //创建资源文件对象 获取加载字典文件
        Properties ResultFileTxt = new Properties();
        try {
            //获取文件中所有的键值对，存放进行ResultFileTxt对象
            ResultFileTxt.load(new FileReader(dictPath));
            return ResultFileTxt.getProperty(word);
        } catch (Exception e) {
            System.out.println("文件不存在");
        }
        return null;
    }

    /**
     * 关闭所有IO流
     */
    public static void CloseAllStream() {
        if (br1 != null) {
            try {
                br1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (br2 != null) {
            try {
                br2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (bw != null) {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
