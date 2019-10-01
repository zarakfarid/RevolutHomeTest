package com.revolut.controller;


import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.revolut.pojo.TransactionRequest;
import com.revolut.processor.TransactionProcessor;

// TODO: Auto-generated Javadoc
/**
 * The Class TransactionController.
 */
@Path("rev/v1")
public class TransactionController {

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(TransactionController.class);

	/** The gson. */
	private static Gson gson = new Gson();

	/**
	 * Transaction commit.
	 *
	 * @param request the request
	 * @return the string
	 */
	@POST
	@Path("transactionCommit")
	public String transactionCommit(String request) {
		logger.info("Request:"+request.toString());
		return TransactionProcessor.commitTransaction(gson.fromJson(request, TransactionRequest.class));
	}


	/**
	 * Transaction status.
	 *
	 * @param transactionId the transaction id
	 * @return the current state of the transaction
	 */
	@GET
	@Path("transactionStatus")
	public String transactionStatus(@QueryParam("transactionId") String transactionId) {
		return TransactionProcessor.transactionStatus(transactionId);
	}

	/**
	 * Gets the account info.
	 *
	 * @param accountNumber the account number
	 * @return the account info
	 */
	@GET
	@Path("accountInfo")
	public String getAccountInfo(@QueryParam("accountNumber") String accountNumber) {
		return gson.toJson(TransactionProcessor.getAccountInfo(accountNumber)).toString();
	}

}
