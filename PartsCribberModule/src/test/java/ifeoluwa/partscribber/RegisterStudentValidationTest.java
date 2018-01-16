package ifeoluwa.partscribber;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RegisterStudentValidationTest
{
    @Test
    public void studentUserNameValidationPositiveTest() throws Exception
    {
        String x = "N01063518";
        assertTrue(PCRegisterStudentFragment.studentUserNameValidation(x));
    }

    @Test
    public void studentUserNameValidationNegativeTest() throws Exception
    {
        String x = "N0@0Ba518";
        assertFalse(PCRegisterStudentFragment.studentUserNameValidation(x));
    }

    @Test
    public void passwordValidationPositiveTest() throws Exception
    {
        String x = "asde@#ll0";
        assertTrue(PCRegisterStudentFragment.passwordValidation(x));
    }

    @Test
    public void passwordValidationNegativeTest() throws Exception
    {
        String x = "ferg l";
        assertFalse(PCRegisterStudentFragment.studentUserNameValidation(x));
    }

    @Test
    public void namesValidationPositiveTest() throws Exception
    {
        String x = "Iverson";
        assertTrue(PCRegisterStudentFragment.namesValidation(x));
    }

    @Test
    public void namesValidationNegativeTest() throws Exception
    {
        String x = "t2";
        assertFalse(PCRegisterStudentFragment.namesValidation(x));
    }
}