package service;

import Entity.Sentence;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 */
public class ChangeTextFile {
    static BufferedReader br_src = null;
    static BufferedReader br_alter = null;
    static BufferedReader br_result = null;
    static BufferedReader br_finally = null;
    static BufferedWriter bw_alter = null;
    static BufferedWriter bw_result = null;
    //设置文件路径
//    static String SrcDirectory = "D:\\a我的文本校对\\自动文件\\0206原文件";//原文件文件夹
//    static String AlterDirectory = "D:\\a我的文本校对\\自动文件\\0206无格式文件";//无格式文件文件夹
//    static String ResultDirectory = "D:\\a我的文本校对\\自动文件\\0206结果文件";//修改后有格式文件文件夹

    //设置文件路径
    static String SrcDirectory = "D:\\a我的文本校对\\0207\\原文件";//原文件文件夹
    static String AlterDirectory = "D:\\a我的文本校对\\自动文件\\0207无格式文件";//无格式文件文件夹
    static String ResultDirectory = "D:\\a我的文本校对\\0207\\结果文件";//修改后有格式文件文件夹

    static String srcFilePath;
    static String resultFilePath;
    static String alterFilePath;
    //字典文件路径
    static String dictPath = "D:\\TextProofreadPlus\\src\\main\\resources\\dictFile\\dictNone.txt";//词组字典
    static String dictWordPath = "D:\\TextProofreadPlus\\src\\main\\resources\\dictFile\\dictWord.txt";//文字字典
    static File dict = new File(dictPath);
    static File dictWord = new File(dictWordPath);

    //解析字典文件
    static Properties ResultFileTxt;
    static Properties ResultFileWord;

    //文本格式
    public static final String status_O = " O";
    public static final String status_B = " B-yinxiangsi";
    public static final String status_I = " I-yinxiangsi";

    //匹配中文字典 汉字正则
    private static final String CHINESE_REGEX = "[\\u4e00-\\u9fa5]";//中文正则范围[\u4e00-\u9fa5]

    public static void main(String[] args) {
        //预加载字典文件
        //创建资源文件对象 获取加载字典文件
        ResultFileTxt = new Properties();
        try {
            //获取文件中所有的键值对，存放进行ResultFileTxt对象
            ResultFileTxt.load(new FileReader(dictPath));
        } catch (Exception e) {
            System.out.println("文件不存在");
        }

        ResultFileWord = new Properties();
        try {
            //获取文件中所有的键值对，存放进行ResultFileTxt对象
            ResultFileWord.load(new FileReader(dictWordPath));
        } catch (Exception e) {
            System.out.println("文件不存在");
        }

        //
        try {
            //删除文件夹下的文件
            //deleteFiles(AlterDirectory);deleteFiles(ResultDirectory);

            // 操作文件夹下的文本文件
            changeAll(SrcDirectory, AlterDirectory, ResultDirectory);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        } finally {
            CloseAllStream();
        }
    }

    /**
     * 修改指定文件夹下的文本文件,输出到指定文件夹下
     *
     * @param SrcDirectory
     * @param ResultDirectory
     * @throws FileNotFoundException
     */
    public static void changeAll(String SrcDirectory, String AlterDirectory, String ResultDirectory) throws IOException {
        File f = new File(SrcDirectory);
        if (!f.exists()) {
            System.out.println(SrcDirectory + " not exists");
            return;
        }
        //获取目录下所有文件
        File fa[] = f.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (fs.isFile()) {
                //设置各个文件路径
                srcFilePath = SrcDirectory + "\\" + fs.getName();
                alterFilePath = AlterDirectory + "\\" + fs.getName();
                resultFilePath = ResultDirectory + "\\" + fs.getName();

                //将原文件转换为无格式文本文件
                //原文件转无格式文件
                ChangeFileToPlain(srcFilePath, alterFilePath);

                //将无格式文本文件转换为修改后格式文本文件
                AlterChange(alterFilePath, resultFilePath);
            }
        }
    }


    /**
     * 读取文件,将文件变成无格式文本
     *
     * @param srcFilePath   源文件地址
     * @param alterFilePath 无格式文件地址
     * @throws IOException
     */
    public static void ChangeFileToPlain(String srcFilePath, String alterFilePath) throws IOException {
        File file = new File(srcFilePath);
        String readLine = "";
        br_src = new BufferedReader(new FileReader(file));
        bw_alter = new BufferedWriter(new FileWriter(alterFilePath));
        while ((readLine = br_src.readLine()) != null) {
            //去掉格式
            readLine = readLine.replaceAll(" O", "");
            bw_alter.write(readLine);
            bw_alter.flush();
        }
    }

    /**
     * 将修改后的句子进行格式转化 输出到修改文件  无格式转化   //可以进行修改为有格式转化
     *
     * @param alterFilePath  无格式文件路径
     * @param resultFilePath 修改后有格式文件路径
     * @throws IOException
     */
    public static void AlterChange(String alterFilePath, String resultFilePath) throws IOException {
        br_alter = new BufferedReader(new FileReader(new File(alterFilePath)));
        bw_result = new BufferedWriter(new FileWriter(new File(resultFilePath)));
        //获取无格式文本
        String AllText = br_alter.readLine();
        //文件分句存储到句子对象
        String[] resultSentences = AllText.split("。");//修改后文件分句
        //修改句子转为集合
        List<Sentence> sentenceList = AlterSentences(resultSentences);


        //将修改后的句子进行格式转化 输出到修改文件 进行修改为有格式转化
        Sentence sentence;//句子对象
        String sentenceText;//句子文本
        String[] status;//状态

        //遍历输出到文件
        for (int i = 0; i < sentenceList.size(); i++) {
            sentence = sentenceList.get(i);
            sentenceText = sentence.getSentenceAlter();
            status = sentence.getStatus();
            for (int j = 0; j < sentenceText.length(); j++) {
                //输出:文字+状态格式
                bw_result.write(sentenceText.charAt(j) + status[j]);
                bw_result.newLine();
                bw_result.flush();
            }
        }
    }


    /**
     * 修改文本 循环修改每个句子
     *
     * @param resultSentences
     * @return
     */
    public static List AlterSentences(String[] resultSentences) throws IOException {

        //创建句子集合
        List<Sentence> sentenceList = new ArrayList<>();
        //将句子还原，添加到句子集合
        for (int i = 0; i < resultSentences.length; i++) {

            sentenceList.add(new Sentence(resultSentences[i] + "。"));
        }

        //循环修改每个句子
        for (int i = 0; i < sentenceList.size(); i++) {
            sentenceList.set(i, AlterSentence(sentenceList.get(i)));
        }

        return sentenceList;
    }

    /**
     * 修改单个句子
     * 判断是否存在书名号等不需要改的格式
     *
     * @param sentence
     * @return
     */
    public static Sentence AlterSentence(Sentence sentence) throws IOException {
        String sentenceText = sentence.getSentenceAlter();//获取当前句子
        String[] status = sentence.getStatus();//存储状态
        String words, findText = "";//存储查找到的词组
        //int countAlter = sentence.getCountAlter();//句子修改次数
        //进行每句话修改词组 修改两次

        //判断是否存在书名号等不需要改的格式
        int indexOfFirstLeft = -1, indexOfFirstRight = -1, indexOfLastLeft = -1, indexOfLastRight = -1;
        if (sentenceText.contains("《") || sentenceText.contains("》") || sentenceText.contains("《》")) {
            indexOfFirstLeft = sentenceText.indexOf("《");
            indexOfFirstRight = sentenceText.indexOf("》");
            indexOfLastLeft = sentenceText.lastIndexOf("《");
            indexOfLastRight = sentenceText.lastIndexOf("》");
        }
        //判断是否存在书名号等不需要改的格式
        int indexOfFirstLeft2 = -1, indexOfFirstRight2 = -1, indexOfLastLeft2 = -1, indexOfLastRight2 = -1;
        if (sentenceText.contains("(") || sentenceText.contains(")") || sentenceText.contains("()")) {
            indexOfFirstLeft2 = sentenceText.indexOf("(");
            indexOfFirstRight2 = sentenceText.indexOf(")");
            indexOfLastLeft2 = sentenceText.lastIndexOf("(");
            indexOfLastRight2 = sentenceText.lastIndexOf(")");
        }

        //遍历修改词组 判断是否为最后两个字，是否满足修改次数
        for (int j = 0; j < sentenceText.length() - 2 && sentence.getCountAlter() < 2; j++) {

            //跳过修改书名号和括号的内容
            //判断如果为《》或（）里的内容则跳过
            if (indexOfFirstLeft != -1 && indexOfFirstRight != -1 || indexOfFirstLeft2 != -1 && indexOfFirstRight2 != -1) {
                if (j >= indexOfFirstLeft && j <= indexOfFirstRight) continue;
                if (j >= indexOfFirstLeft2 && j <= indexOfFirstRight2) continue;
            }
            //判断如果为《》或（）里的内容则跳过
            if (indexOfLastLeft != -1 && indexOfLastRight != -1 || indexOfLastLeft2 != -1 && indexOfLastRight2 != -1) {
                if (j >= indexOfLastLeft && j <= indexOfLastRight) continue;
                if (j >= indexOfLastLeft2 && j <= indexOfLastRight2) continue;
            }

            //查找单词
            words = sentenceText.substring(j, j + 2);//j+1,j+2位置
            findText = ResultFileTxt.getProperty(words);//查找可以修改的词组
            //不满足条件 查找下一个
            if (findText != null && !findText.equals("")) {
                //满足条件 进行修改 状态改为B 和 I
                sentenceText = sentenceText.replaceFirst(words, findText);//替换存在字典的词组
                status[j] = status_B;
                status[j + 1] = status_I;
                j++;//跳过这个词组
                sentence.setCountAlter(sentence.getCountAlter() + 1);//修改次数加一
                //避免连续修改
                j++;
                status[j] = status_O;
            } else {
                //不满足 状态改为O
                status[j] = status_O;
            }

        }
        //修改后的句子存入对象sentenceAlter  状态存入对象
        sentence.setSentenceAlter(sentenceText);
        sentence.setStatus(status);
        //如果不满足修改两次  判断句子条件修改单个文字
        if (sentence.getCountAlter() < 2) {
            sentence = AlterWord(sentence);
        }
        return sentence;
    }

    /**
     * 修改句子中的单个文字
     *
     * @param sentence
     * @return
     */
    public static Sentence AlterWord(Sentence sentence) {
        String sentenceText = sentence.getSentenceAlter();
        String[] status = sentence.getStatus();
        int SLength = sentenceText.length();//句子文字数  修改为汉字字数
        //情况一:句子长  修改次数小于两次
        String word, words, findText = "";//存储查找到的词组


        //判断是否存在书名号等不需要改的格式
        int indexOfFirstLeft = -1, indexOfFirstRight = -1, indexOfLastLeft = -1, indexOfLastRight = -1;
        if (sentenceText.contains("《") || sentenceText.contains("》") || sentenceText.contains("《》")) {
            indexOfFirstLeft = sentenceText.indexOf("《");
            indexOfFirstRight = sentenceText.indexOf("》");
            indexOfLastLeft = sentenceText.lastIndexOf("《");
            indexOfLastRight = sentenceText.lastIndexOf("》");
        }
        int indexOfFirstLeft2 = -1, indexOfFirstRight2 = -1, indexOfLastLeft2 = -1, indexOfLastRight2 = -1;
        if (sentenceText.contains("(") || sentenceText.contains(")") || sentenceText.contains("()")) {
            indexOfFirstLeft2 = sentenceText.indexOf("(");
            indexOfFirstRight2 = sentenceText.indexOf(")");
            indexOfLastLeft2 = sentenceText.lastIndexOf("(");
            indexOfLastRight2 = sentenceText.lastIndexOf(")");
        }


        //
        if (SLength > 15) {
            //进行每句话修改单个文字 找到对应的前后存在修改的一个文字的两个文本
            for (int j = sentenceText.length() - 1; j > 0 && sentence.getCountAlter() < 2; j--) {

                //
                if (indexOfFirstLeft != -1 && indexOfFirstRight != -1 || indexOfFirstLeft2 != -1 && indexOfFirstRight2 != -1) {
                    if (j >= indexOfFirstLeft && j <= indexOfFirstRight) continue;
                    if (j >= indexOfFirstLeft2 && j <= indexOfFirstRight2) continue;
                }
                if (indexOfLastLeft != -1 && indexOfLastRight != -1 || indexOfLastLeft2 != -1 && indexOfLastRight2 != -1) {
                    if (j >= indexOfLastLeft && j <= indexOfLastRight) continue;
                    if (j >= indexOfLastLeft2 && j <= indexOfLastRight2) continue;
                }


                //判断当前词是否已经被修改 已经修改则跳到下一个
                if (!status[j].equals(status_O)) continue;

                words = String.valueOf(sentenceText.charAt(j));//获取当前文字
                findText = ResultFileWord.getProperty(words);//查找可以修改的文字
                //不满足条件 查找下一个
                if (findText != null && !findText.equals("")) {
                    //满足条件 进行修改
                    sentenceText = sentenceText.replaceFirst(words, findText);//替换存在字典的词组
                    status[j] = status_B;//修改后状态
                    sentence.setCountAlter(sentence.getCountAlter() + 1);//修改次数加一
                    j -= 3;//跳开修改
                }
            }
        } else if (SLength <= 15) {//情况二:句子短 修改次数小于两次 此时只要修改一次
            for (int j = sentenceText.length() - 1; j > 0 && sentence.getCountAlter() < 1; j--) {

                //
                if (indexOfFirstLeft != -1 && indexOfFirstRight != -1 || indexOfFirstLeft2 != -1 && indexOfFirstRight2 != -1) {
                    if (j >= indexOfFirstLeft && j <= indexOfFirstRight) continue;
                    if (j >= indexOfFirstLeft2 && j <= indexOfFirstRight2) continue;
                }
                if (indexOfLastLeft != -1 && indexOfLastRight != -1 || indexOfLastLeft2 != -1 && indexOfLastRight2 != -1) {
                    if (j >= indexOfLastLeft && j <= indexOfLastRight) continue;
                    if (j >= indexOfLastLeft2 && j <= indexOfLastRight2) continue;
                }


                //判断当前词是否已经被修改 已经修改则跳到下一个
                if (!status[j].equals(status_O)) continue;

                words = String.valueOf(sentenceText.charAt(j));//
                findText = ResultFileWord.getProperty(words);//查找可以修改的词组
                //不满足条件 查找下一个
                if (findText != null && !findText.equals("")) {
                    //满足条件 进行修改
                    sentenceText = sentenceText.replaceFirst(words, findText);//替换存在字典的词组
                    status[j] = status_B;//修改后状态
                    sentence.setCountAlter(sentence.getCountAlter() + 1);//修改次数加一
                    j--;
                }
            }
        }
        sentence.setSentenceAlter(sentenceText);
        sentence.setStatus(status);

        if (sentence.getCountAlter() < 2) System.out.println(sentenceText);

        return sentence;
    }

    /**
     * 删除文件夹下所有文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFiles(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }


    /**
     * 关闭所有IO流
     */
    public static void CloseAllStream() {
        if (br_src != null) {
            try {
                br_src.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (br_alter != null) {
            try {
                br_alter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (br_finally != null) {
            try {
                br_finally.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (br_result != null) {
            try {
                br_result.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (bw_alter != null) {
            try {
                bw_alter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (bw_result != null) {
            try {
                bw_result.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (bw_result != null) {
            try {
                bw_result.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
