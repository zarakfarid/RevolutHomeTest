package com.revolut.model;

// TODO: Auto-generated Javadoc
/**
 * The Class Account.
 */
public class Account {

	/** The name. */
	String name;

	/** The account number. */
	String accountNumber;

	/** The address. */
	String address;

	/** The money. */
	Double money;

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the account number.
	 *
	 * @return the account number
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * Sets the account number.
	 *
	 * @param accountNumber the new account number
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Gets the money.
	 *
	 * @return the money
	 */
	public Double getMoney() {
		return money;
	}

	/**
	 * Sets the money.
	 *
	 * @param money the new money
	 */
	public void setMoney(Double money) {
		this.money = money;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Account [name=" + name + ", accountNumber=" + accountNumber + ", address=" + address + ", money="
				+ money +"]";
	}

}
