package com.revolut.constants;

// TODO: Auto-generated Javadoc
/**
 * The ReportingConstants Class.
 */
public class DBConstants {

	/** The Constant DB_DRIVER. */
	public static final String DB_DRIVER = "org.h2.Driver";

	/** The Constant DB_CONNECTION. */
	public static final String DB_CONNECTION = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";

	/** The Constant DB_USER. */
	public static final String DB_USER = "";

	/** The Constant DB_PASSWORD. */
	public static final String DB_PASSWORD = "";
	
	/** The Constant DB_SQL_CREATE_ACCOUNT_TABLE. */
	public static final String DB_SQL_CREATE_ACCOUNT_TABLE = "create table accounts(accountNumber varchar(22) primary key, name varchar(255), address varchar(255), money double)";
	
	/** The Constant DB_SQL_ACCOUNT_INSERT. */
	public static final String DB_SQL_ACCOUNT_INSERT = "insert into accounts (name, accountNumber, address, money) values (?, ?, ?, ?)";
	
	/** The Constant DB_SQL_ACCOUNT_SELECT. */
	public static final String DB_SQL_ACCOUNT_SELECT = "select * from accounts where accountNumber = ? and money >= ?";

	/** The Constant DB_SQL_ACCOUNT_INFO. */
	public static final String DB_SQL_ACCOUNT_INFO = "select * from accounts where accountNumber = ?";

	/** The Constant DB_SQL_ACCOUNT_EXISTS. */
	public static final String DB_SQL_ACCOUNT_EXISTS = "select name from accounts where accountNumber = ?";

	/** The Constant DB_SQL_ACCOUNT_ADD. */
	public static final String DB_SQL_ACCOUNT_ADD = "UPDATE accounts SET money = money + ? WHERE accountNumber = ?";

	/** The Constant DB_SQL_ACCOUNT_SUBTRACT. */
	public static final String DB_SQL_ACCOUNT_SUBTRACT = "UPDATE accounts SET money = money - ? WHERE accountNumber = ?";
}
