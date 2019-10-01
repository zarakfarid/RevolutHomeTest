package com.revolut.utils;

// TODO: Auto-generated Javadoc
/**
 * The Class CommonUtils.
 */
public class CommonUtils {
	
	/**
	 * Checks if is bank account number is valid.
	 *
	 * @param number the number
	 * @return true, if is bank account number
	 */
	public static boolean isBankAccountNumber(String number) {
		if (number != null && number.length() == 22 && number.substring(0, 2).equals("DE")) {
			return true;
		} else {
			return false;
		}
	}
}
