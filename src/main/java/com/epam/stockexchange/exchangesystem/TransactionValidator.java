package com.epam.stockexchange.exchangesystem;

import com.epam.stockexchange.entity.Participant;
import com.epam.stockexchange.exception.TransactionException;

import java.math.BigDecimal;

public class TransactionValidator {

    public void validateTransaction(Participant firstParticipant, Participant secondParticipant, TransactionType transactionType, BigDecimal amountToGive, BigDecimal amountToReceive) throws TransactionException {
        if (firstParticipant.equals(secondParticipant)) {
            throw new TransactionException("Cannot perform a transaction with yourself");
        }
        TransactionException firstParticipantException = new TransactionException("Not enough currency on account");
        TransactionException secondParticipantException = new TransactionException("Not enough currency on second participant's account");
        switch (transactionType) {
            case USD_TO_BYN:
                if (firstParticipant.getUsd()
                        .compareTo(amountToGive) < 0) {
                    throw firstParticipantException;
                }
                if (secondParticipant.getByn()
                        .compareTo(amountToReceive) < 0) {
                    throw secondParticipantException;
                }
                break;
            case USD_TO_EUR:
                if (firstParticipant.getUsd()
                        .compareTo(amountToGive) < 0) {
                    throw firstParticipantException;
                }
                if (secondParticipant.getEur()
                        .compareTo(amountToReceive) < 0) {
                    throw secondParticipantException;
                }
                break;
            case BYN_TO_EUR:
                if (firstParticipant.getByn()
                        .compareTo(amountToGive) < 0) {
                    throw firstParticipantException;
                }
                if (secondParticipant.getEur()
                        .compareTo(amountToReceive) < 0) {
                    throw secondParticipantException;
                }
                break;
            case BYN_TO_USD:
                if (firstParticipant.getByn()
                        .compareTo(amountToGive) < 0) {
                    throw firstParticipantException;
                }
                if (secondParticipant.getUsd()
                        .compareTo(amountToReceive) < 0) {
                    throw secondParticipantException;
                }
                break;
            case EUR_TO_BYN:
                if (firstParticipant.getEur()
                        .compareTo(amountToGive) < 0) {
                    throw firstParticipantException;
                }
                if (secondParticipant.getByn()
                        .compareTo(amountToReceive) < 0) {
                    throw secondParticipantException;
                }
                break;
            case EUR_TO_USD:
                if (firstParticipant.getEur()
                        .compareTo(amountToGive) < 0) {
                    throw firstParticipantException;
                }
                if (secondParticipant.getUsd()
                        .compareTo(amountToReceive) < 0) {
                    throw secondParticipantException;
                }
                break;
            default:
                throw new TransactionException("Unsupported transaction type");
        }
        if (amountToGive.doubleValue() < 0
                || amountToReceive.doubleValue() < 0) {
            throw new TransactionException("Transaction amounts cannot be negative");
        }
    }

}
