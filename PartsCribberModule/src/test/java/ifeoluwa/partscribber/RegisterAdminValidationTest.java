package ifeoluwa.partscribber;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RegisterAdminValidationTest
{
    @Test
    public void adminUsernameValidationPositiveTest() throws Exception
    {
        String x = "adminKelly";
        assertTrue(PCRegisterAdminFragment.adminUsernameValidation(x));
    }

    @Test
    public void adminUsernameValidationNegativeTest() throws Exception
    {
        String x = "trey5";
        assertFalse(PCRegisterAdminFragment.adminUsernameValidation(x));
    }

    @Test
    public void adminPasswordValidationPositiveTest() throws Exception
    {
        String x = "ode@rty35";
        assertTrue(PCRegisterAdminFragment.passwordValidation(x));
    }

    @Test
    public void adminPasswordValidationNegativeTest() throws Exception
    {
        String x = "abcdefghijklmnop";
        assertFalse(PCRegisterAdminFragment.passwordValidation(x));
    }

    @Test
    public void namesValidationPositiveTest() throws Exception
    {
        String x = "Craig";
        assertTrue(PCRegisterAdminFragment.namesValidation(x));
    }

    @Test
    public void namesValidationNegativeTest() throws Exception
    {
        String x = "  4 %r";
        assertFalse(PCRegisterAdminFragment.namesValidation(x));
    }
}