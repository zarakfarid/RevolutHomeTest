package com.revolut.processor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revolut.constants.TransactionAppConstants;
import com.revolut.dao.TransactionDao;
import com.revolut.model.Account;
import com.revolut.pojo.TransactionRequest;
import com.revolut.thread.TransactionPool;
import com.revolut.thread.TransactionTask;
import com.revolut.utils.CommonUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class TransactionProcessor.
 */
public class TransactionProcessor {

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(TransactionProcessor.class);

	/** The transaction id. */
	private static long transactionId = 0;

	/** The queue. */
	private static HashMap<String, LinkedList<TransactionRequest>> queue = new HashMap<>();

	/** The transactions state. */
	private static HashMap<Long, String> transactionsState = new HashMap<>();

	/**
	 * Commit transaction.
	 *
	 * @param request the request
	 * @return the string
	 */
	public static String commitTransaction(TransactionRequest request) {

		transactionId++;
		
		///Setting up a Transaction Id:
		request.setId(transactionId);
		///Updating Transaction State:
		transactionsState.put(transactionId, TransactionAppConstants.TRANSACTION_STATUS_IN_PROCESS);
		
		// Validate Request (In this case both the account numbers and the amount)
		if (CommonUtils.isBankAccountNumber(request.getFrom()) && CommonUtils.isBankAccountNumber(request.getTo())
				&& request.getAmount() != null && request.getAmount() > 0) {

			//If a request from the same account is in process put it in a queue 
			//else execute the request by assigning it a thread from the thread pool
			if (queue.containsKey(request.getFrom()) && queue.get(request.getFrom()).size() > 0) {
				logger.info("Transaction in queue, id:"+transactionId);
				queue.get(request.getFrom()).add(request);
				return "Transaction Queued with Id:" + request.getId();
			} else {
				LinkedList<TransactionRequest> list = new LinkedList<>();
				list.add(request);
				queue.put(request.getFrom(), list);
				TransactionPool.commitTransaction(request);
				return "Transaction Started with Id:" + request.getId();
			}

		}else {
			return "Fault in Transaction, Please provide correct data," + request.toString();
		}
	}

	/**
	 * Transaction status.
	 *
	 * @param transactionId the transaction id
	 * @return the current state of the transaction
	 */
	public static String transactionStatus(String transactionId) {
		return transactionsState.get(Long.valueOf(transactionId));
	}

	/**
	 * Dequeue.
	 *
	 * @param accountNumber, the account from a transaction is finished
	 * @param state the final state of the transaction
	 */
	public static void dequeue(String accountNumber, String state) {
		if (queue.containsKey(accountNumber)) {
			///Removing request from queue:
			TransactionRequest request = queue.get(accountNumber).remove();
			///Updating Transaction State:
			transactionsState.put(request.getId(), state);

			///Dequeuing request:
			if (queue.get(accountNumber).size() > 0) {
				TransactionPool.commitTransaction(queue.get(accountNumber).peek());
			} else {
				queue.remove(accountNumber);
			}
		}
	}

	/**
	 * Gets the account info.
	 *
	 * @param accountNumber the account number
	 * @return the account info
	 */
	public static Account getAccountInfo(String accountNumber) {
		return TransactionDao.getAccountInfo(accountNumber);
	}

}
