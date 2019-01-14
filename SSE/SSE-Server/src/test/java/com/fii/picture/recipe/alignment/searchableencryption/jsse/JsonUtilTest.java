package jsse;

import com.fii.picture.recipe.alignment.searchableencryption.service.JsonUtil;
import com.fii.picture.recipe.alignment.searchableencryption.service.SSEUtil;
import com.fii.picture.recipe.alignment.searchableencryption.service.SWP;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.util.List;

/**
 * Created by Ariana on 1/9/2019.
 */
public class JsonUtilTest extends TestCase
{
    private String password;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public JsonUtilTest (String  testName)

    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( JsonUtilTest.class );
    }

    public void setUp() throws Exception {
        super.setUp();

        password = "test"; // NOT FOR PRODUCTION
        Security.addProvider(new BouncyCastleProvider());
    }


    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testJsonUtil() throws Exception {
        System.out.println("Test AES Searchable ");

        double loadFactor = 1; // No false positives but additional storage
        try {
            final SecretKeySpec secretKeySpec = SSEUtil.getSecretKeySpec(password,
                    SSEUtil.getRandomBytes(20));
            SWP swp = new SWP(secretKeySpec, "AES",loadFactor, 128);

            JsonUtil.swp =swp;
            JsonUtil.translateToDecryptedJSON();
            JsonUtil.translateToEncryptedJSON();
            byte[] searchPlainText = ("milk").getBytes();
            List<Integer> recordIds = JsonUtil.searchEncryptedJSON(searchPlainText);
            System.out.println(recordIds.size());
            for(Integer i : recordIds){
                System.out.println(i);
                System.out.println("\n");
            }

            if(recordIds.size() == 5)
                assertTrue("Found all ingredients", true);


        } catch (Exception e){
            e.printStackTrace();
            assertTrue("Something went wrong .. some where !!! .." + e.getMessage(),false);
        }
    }
}