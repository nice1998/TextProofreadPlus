package TextProofreadPlus;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void exists() {
       File AlterTextFile = new File("D:\\a我的文本校对\\AlterTextFile");

       if (AlterTextFile.exists()){
           System.out.println("文件存在");
       }else{
           System.out.println("文件不存在");
       }
    }
}
