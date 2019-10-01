package com.revolut.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.revolut.constants.TransactionAppConstants;
import com.revolut.pojo.TransactionRequest;

// TODO: Auto-generated Javadoc
/**
 * The Class TransactionPool.
 */
public class TransactionPool {

	/** The executor. */
	public static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors
			.newFixedThreadPool(TransactionAppConstants.CONCURRENT_TRANSACTION);

	/**
	 * Commit transaction.
	 *
	 * @param request, the request for transferring money
	 */
	public static void commitTransaction(TransactionRequest request) {
		executor.execute(new TransactionTask(request.getId(), request.getFrom(), request.getTo(), request.getAmount()));
	}

}
