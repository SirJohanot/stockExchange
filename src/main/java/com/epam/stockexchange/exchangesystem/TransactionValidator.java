package com.epam.stockexchange.exchangesystem;

import com.epam.stockexchange.entity.Participant;
import com.epam.stockexchange.exception.TransactionException;

public class TransactionValidator {

    public void validateTransaction(Participant firstParticipant, Participant secondParticipant, TransactionType transactionType, double amountToGive, double amountToReceive) throws TransactionException {
        TransactionException firstParticipantException = new TransactionException("Not enough currency on account");
        TransactionException secondParticipantException = new TransactionException("Not enough currency on second participant's account");
        switch (transactionType) {
            case USD_TO_BYN:
                if (firstParticipant.getUsd() < amountToGive) {
                    throw firstParticipantException;
                }
                if (secondParticipant.getByn() < amountToReceive) {
                    throw secondParticipantException;
                }
                break;
            case USD_TO_EUR:
                if (firstParticipant.getUsd() < amountToGive) {
                    throw firstParticipantException;
                }
                if (secondParticipant.getEur() < amountToReceive) {
                    throw secondParticipantException;
                }
                break;
            case BYN_TO_EUR:
                if (firstParticipant.getByn() < amountToGive) {
                    throw firstParticipantException;
                }
                if (secondParticipant.getEur() < amountToReceive) {
                    throw secondParticipantException;
                }
                break;
            case BYN_TO_USD:
                if (firstParticipant.getByn() < amountToGive) {
                    throw firstParticipantException;
                }
                if (secondParticipant.getUsd() < amountToReceive) {
                    throw secondParticipantException;
                }
                break;
            case EUR_TO_BYN:
                if (firstParticipant.getEur() < amountToGive) {
                    throw firstParticipantException;
                }
                if (secondParticipant.getByn() < amountToReceive) {
                    throw secondParticipantException;
                }
                break;
            case EUR_TO_USD:
                if (firstParticipant.getEur() < amountToGive) {
                    throw firstParticipantException;
                }
                if (secondParticipant.getUsd() < amountToReceive) {
                    throw secondParticipantException;
                }
                break;
            default:
                throw new TransactionException("Unsupported transaction type");
        }
        if (amountToGive < 0 || amountToReceive < 0) {
            throw new TransactionException("Transaction amounts cannot be negative");
        }
        if (!secondParticipant.transactionQuery()) {
            throw new TransactionException("The second participant did not agree to the transaction");
        }
    }

}
