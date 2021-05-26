package Entity;

/**
 * @author Mr.Xu
 * @version 1.0
 * @description: TODO
 * @date 2021/5/21 10:23
 */
public class Sentence {

    public static final String status_O=" O";//原文本格式
    public static final String status_B="B-yinxiangsi";//替换词格式
    public static final String status_I="I-yinxiangsi";//替换词组格式
    private String sentence;//原来句子
    private String sentenceAlter;//修改后句子
    private String[] status;//文字当前的状态
    private int countAlter=0;//修改次数


    public Sentence() {
    }

    /**
     * 初始化格式
     * @param sentence
     */
    public Sentence(String sentence) {
        this.sentence = sentence;
        this.sentenceAlter=sentence;
        this.status=new String[sentence.length()];
        for (int i = 0; i < sentence.length(); i++) {
            this.status[i]=status_O;
        }
    }

    public Sentence(String sentenceAlter, String[] status) {
        this.sentenceAlter = sentenceAlter;
        this.status = status;
    }

    public Sentence(String sentence, String sentenceAlter, String[] status) {
        this.sentence = sentence;
        this.sentenceAlter = sentenceAlter;
        this.status = status;
    }

    public Sentence(String sentence, String sentenceAlter, String[] status, int countAlter) {
        this.sentence = sentence;
        this.sentenceAlter = sentenceAlter;
        this.status = status;
        this.countAlter = countAlter;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getSentenceAlter() {
        return sentenceAlter;
    }

    public void setSentenceAlter(String sentenceAlter) {
        this.sentenceAlter = sentenceAlter;
    }

    public String[] getStatus() {
        return status;
    }

    public void setStatus(String[] status) {
        this.status = status;
    }

    public int getCountAlter() {
        return countAlter;
    }

    public void setCountAlter(int countAlter) {
        this.countAlter = countAlter;
    }
}
