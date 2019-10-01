package com.revolut.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.revolut.constants.DBConstants;
import com.revolut.constants.TransactionAppConstants;
import com.revolut.model.Account;

// TODO: Auto-generated Javadoc
/**
 * The Class TransactionDao.
 */
public class TransactionDao {

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(TransactionDao.class);

	/** The gson. */
	private static Gson gson = new Gson();

	/**
	 * Intialize the database with all the data
	 */
	public static void intialize() {
		Connection connection = getDBConnection();
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			stmt.execute(DBConstants.DB_SQL_CREATE_ACCOUNT_TABLE);

			PreparedStatement ps = connection.prepareStatement(DBConstants.DB_SQL_ACCOUNT_INSERT);
			ArrayList<Account> accounts = getAccountData();
			for (Account account : accounts) {

				ps.setString(1, account.getName());
				ps.setString(2, account.getAccountNumber());
				ps.setString(3, account.getAddress());
				ps.setDouble(4, account.getMoney());
				ps.addBatch();
			}
			ps.executeBatch();
			ps.close();

			stmt.close();
			logger.info("Intialized Test Data Succesfully");
		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				logger.error("Exception in closing connection");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Transfer.
	 *
	 * @param from, the account to transfer from
	 * @param to, the account to transfer to
	 * @param amount the amount of money to transfer
	 * @return true, if successful
	 */
	public static boolean transfer(String from, String to, Double amount) {

		Connection connection = getDBConnection();
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(DBConstants.DB_SQL_ACCOUNT_SUBTRACT);
			ps.setDouble(1, amount);
			ps.setString(2, from);
            int row = ps.executeUpdate();
			if (row > 0) {
				ps.close();
				ps = connection.prepareStatement(DBConstants.DB_SQL_ACCOUNT_ADD);
				ps.setDouble(1, amount);
				ps.setString(2, to);
	            row = ps.executeUpdate();
				if (row > 0) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (SQLException e) {
			logger.error("Exception in executing sql statement");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				connection.close();
			} catch (SQLException e) {
				logger.error("Exception in closing connection");
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * Verify if the account and the money can be transferred
	 *
	 * @param from, the account to transfer from
	 * @param to, the account to transfer to
	 * @param amount the amount of money to transfer
	 * @return true, if successful
	 */
	public static boolean verify(String from, String to, Double amount) {
		Connection connection = getDBConnection();
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(DBConstants.DB_SQL_ACCOUNT_SELECT);
			ps.setString(1, from);
			ps.setDouble(2, amount);
			
			if (ps.executeQuery().first()) {
				ps.close();
				ps = connection.prepareStatement(DBConstants.DB_SQL_ACCOUNT_EXISTS);
				ps.setString(1, to);
				if (ps.executeQuery().first()) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (SQLException e) {
			logger.error("Exception in executing sql statement");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				connection.close();
			} catch (SQLException e) {
				logger.error("Exception in closing connection");
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * Gets the account data.
	 *
	 * @return the account data
	 */
	private static ArrayList<Account> getAccountData() {
		ArrayList<Account> result = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(TransactionDao.class.getResourceAsStream(TransactionAppConstants.TEST_DATA_FILE)));

			String row = null;
			while ((row = reader.readLine()) != null) {
				result.add(gson.fromJson(row, Account.class));
			}
		} catch (Exception e) {
			logger.error("Exception in Parsing");
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Gets the DB connection.
	 *
	 * @return the DB connection
	 */
	private static Connection getDBConnection() {
		Connection dbConnection = null;
		try {
			dbConnection = DriverManager.getConnection(DBConstants.DB_CONNECTION, DBConstants.DB_USER,
					DBConstants.DB_PASSWORD);
			return dbConnection;
		} catch (Exception e) {
			logger.error("Exception in acquiring connection");
			e.printStackTrace();
		}
		return dbConnection;
	}

	/**
	 * Gets the account info.
	 *
	 * @param accountNumber the account number for getting info
	 * @return the account info
	 */
	public static Account getAccountInfo(String accountNumber) {
		Account result = null;

		Connection connection = getDBConnection();
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(DBConstants.DB_SQL_ACCOUNT_INFO);
			ps.setString(1, accountNumber);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
            	result = new Account();
            	result.setAccountNumber(accountNumber);
            	result.setAddress(resultSet.getString("address"));
            	result.setMoney(resultSet.getDouble("money"));
            	result.setName(resultSet.getString("name"));
            }
		} catch (SQLException e) {
			logger.error("Exception in executing sql statement");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				connection.close();
			} catch (SQLException e) {
				logger.error("Exception in closing connection");
				e.printStackTrace();
			}
		}
		return result;	
	}

}
