package ifeoluwa.partscribber;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RegisterEquipmentValidationTest
{
    @Test
    public void lengthValidationPositiveTest() throws Exception
    {
        String x = "Ethernet Cable"; //Item name is required and should be 3 or more characters.
        assertTrue(PartsCribberViewEquipment.lengthValidation(x));
    }

    @Test
    public void lengthValidationNegativeTest() throws Exception
    {
        String x = "2A";//Item name is required and should be 3 or more characters.
        assertFalse(PartsCribberViewEquipment.lengthValidation(x));
    }

    @Test
    public void serialNumberValidationPositiveTest() throws Exception
    {
        String x = "336672"; //Serial number is required and has to be exactly 6 digits.
        assertTrue(PartsCribberViewEquipment.serialNumberValidation(x));
    }

    @Test
    public void serialNumberValidationNegativeTest() throws Exception
    {
        String x = "bax231";//Serial number is required and has to be exactly 6 digits.
        assertFalse(PartsCribberViewEquipment.serialNumberValidation(x));
    }

    @Test
    public void digitValidationPositiveTest() throws Exception
    {
        String x = "0012"; //Stock quantity has to be at least 1.
        assertTrue(PartsCribberViewEquipment.digitValidation(x));
    }

    @Test
    public void digitValidationNegativeTest() throws Exception
    {
        String x = "00000"; //Stock quantity has to be at least 1.
        assertFalse(PartsCribberViewEquipment.digitValidation(x));
    }
}