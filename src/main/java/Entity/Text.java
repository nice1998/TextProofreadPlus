package Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr.Xu
 * @version 1.0
 * @description: TODO
 * @date 2021/5/21 10:23
 */
public class Text {


    //存储文本所有句子
    private static List<Sentence> sentences=new ArrayList();

    public Text() {
//        初始化文本
//        将文本分割为句子

    }

    public Text(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    public List<Sentence> getSentenceList() {
        return sentences;
    }

    public void setSentenceList(List<Sentence> sentences) {
        this.sentences = sentences;
    }
}
