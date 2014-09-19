package test;

import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.TestCase;
import model.CannotCreateDirectoryException;
import model.HumanUser;
import model.UnregisteredUserException;
import model.User;
import model.UserAlreadyRegisteredException;
import model.UsersRegister;

import org.junit.Test;

/**
 * 
 * Tests for the {@link User} class
 * 
 * @author Team AFK
 * @version 1.0
 * 
 */

public class TestUser extends TestCase {

	// TODO more tests on the User(s)

	/**
	 * Tests that Users are well stored inside the {@link UsersRegister}
	 */
	@Test
	public void testUsersRegister() {
		UsersRegister register = UsersRegister.getInstance();
		try {
			System.out.println("=====\nregister before adding : \n"+register.toString()+"\n=====\n");
			register.addUser("Jean");
			register.addUser("Alexandre");
			register.addUser("Farouk");
			System.out.println("=====\nregister after adding : \n"+register.toString()+"\n=====\n");
			 register.saveFile();
			User jean = new HumanUser("Jean");
			User alexandre = new HumanUser("Alexandre");
			User farouk = new HumanUser("Farouk");
			assertEquals(jean, register.getUser("Jean"));
			assertEquals(alexandre, register.getUser("Alexandre"));
			assertEquals(farouk, register.getUser("Farouk"));
			
			register.addUser("Jean");

			fail("UserAlreadyRegisteredException not thrown");

		} catch (UserAlreadyRegisteredException e) {
			System.err.println(e.getMessage());
		} catch (UnregisteredUserException e) {
			System.err.println(e.getMessage());
		} catch (CannotCreateDirectoryException e) {
			System.err.println(e.getMessage());
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

}
