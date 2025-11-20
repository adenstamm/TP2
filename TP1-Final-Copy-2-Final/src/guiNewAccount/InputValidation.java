package guiNewAccount;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class InputValidation {

	@Test
	public void testUsername1() {
		assertEquals("", guiNewAccount.ModelNewAccount.evaluateUsername("Ianj"));
	}
	@Test
	public void testUsername2() {
		assertNotEquals("", guiNewAccount.ModelNewAccount.evaluateUsername("1234"));
	}
	@Test
	public void testUsername3() {
		assertEquals("", guiNewAccount.ModelNewAccount.evaluateUsername("Ian1"));
	}
	@Test
	public void testUsername4() {
		assertNotEquals("", guiNewAccount.ModelNewAccount.evaluateUsername("2Ian"));
	}
	@Test
	public void testUsername5() {
		assertNotEquals("", guiNewAccount.ModelNewAccount.evaluateUsername("Aaaaaaaaaaaaaaaaa"));
	}
	@Test
	
	public void testUsername6() {
		assertNotEquals("", guiNewAccount.ModelNewAccount.evaluateUsername("Ian"));
	}
	@Test
	public void testUsername7() {
		assertNotEquals("", guiNewAccount.ModelNewAccount.evaluateUsername("Ian2#"));
	}
	
	
	@Test
	public void testPassword1() {
		assertEquals("", guiNewAccount.ModelNewAccount.evaluatePassword("12345aA."));
	}
	@Test
	public void testPassword2() {
		assertNotEquals("", guiNewAccount.ModelNewAccount.evaluatePassword("aaaaaaaa")); //No special characters or numbers
	}
	@Test
	public void testPassword3() {
		assertNotEquals("", guiNewAccount.ModelNewAccount.evaluatePassword("12345aAA")); //No special character
	}
	@Test
	public void testPassword4() {
		assertNotEquals("", guiNewAccount.ModelNewAccount.evaluatePassword("12345678")); //No alphabetic character or special character
	}
	@Test
	public void testPassword5() {
		assertNotEquals("", guiNewAccount.ModelNewAccount.evaluatePassword("1234aA.")); //Too short
	}
	@Test
	public void testPassword6() {
		assertNotEquals("", guiNewAccount.ModelNewAccount.evaluatePassword("123456789abcdefghijklmnopqrstuvwxyzA.")); //Too long
	}
	@Test
	public void testPassword7() {
		assertNotEquals("", guiNewAccount.ModelNewAccount.evaluatePassword("abcdefg.")); //No numbers
	}
	@Test
	public void testPassword8() {
		assertNotEquals("", guiNewAccount.ModelNewAccount.evaluatePassword("")); //No input
	}
	@Test
	public void testPassword9() {
		assertNotEquals("", guiNewAccount.ModelNewAccount.evaluatePassword("1234 aBc.")); //Has an invalid character
	}
	
}
