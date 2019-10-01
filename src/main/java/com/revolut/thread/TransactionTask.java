package com.revolut.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revolut.constants.TransactionAppConstants;
import com.revolut.dao.TransactionDao;
import com.revolut.processor.TransactionProcessor;

// TODO: Auto-generated Javadoc
/**
 * The Class TransactionTask.
 * The Thread to transfer money from one account to another
 */
public class TransactionTask implements Runnable {

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(TransactionTask.class);

	/** The from. */
	String from;

	/** The to. */
	String to;

	/** The amount. */
	Double amount;

	/** The transaction id. */
	long transactionId;

	/**
	 * Instantiates a new transaction task.
	 *
	 * @param transactionId the transaction id
	 * @param from the from
	 * @param to the to
	 * @param amount the amount
	 */
	public TransactionTask(long transactionId, String from, String to, Double amount) {
		this.from = from;
		this.to = to;
		this.amount = amount;
		this.transactionId = transactionId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		logger.info("Transaction in progress, id:"+transactionId);
		String result = TransactionAppConstants.TRANSACTION_STATUS_FAIL;
		
		try {
			//Just to see if the queuing works
			Thread.sleep(2000); 
			if(TransactionDao.verify(from, to, Double.valueOf(amount))) {
				logger.info("Account Verified");
				if(TransactionDao.transfer(from, to, Double.valueOf(amount))) {
					logger.info("Amount Transferred");
					result = TransactionAppConstants.TRANSACTION_STATUS_SUCCESS;
				}else {
					//verified but not transfered
					result = TransactionAppConstants.TRANSACTION_STATUS_FAIL;
				}
			}else {
				//account number and amount cannot be verified
				result = TransactionAppConstants.TRANSACTION_STATUS_FAIL;
			}
		} catch (Exception e) {
			//exception while transferring money
			result = TransactionAppConstants.TRANSACTION_STATUS_FAIL;
			e.printStackTrace();
		}
		logger.info("Transaction ended, id:"+transactionId);
		TransactionProcessor.dequeue(from, result);
	}

}
