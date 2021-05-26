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

       }else{
           AlterTextFile.mkdir();
       }
    }
}
