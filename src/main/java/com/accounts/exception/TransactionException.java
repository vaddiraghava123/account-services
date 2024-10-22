package com.accounts.exception;

/**
 * Transaction Exception Handler
 */
public class TransactionException  extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public TransactionException(String msg){
        super(msg);
    }

}
