package com.epam.stockexchange.exchangesystem;

import com.epam.stockexchange.entity.Participant;
import com.epam.stockexchange.exception.TransactionException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StockExchangePlatform {

    public static final double BYN_TO_EUR_RATE = 2.75;
    public static final double EUR_TO_BYN_RATE = 0.36;
    public static final double BYN_TO_USD_RATE = 2.5;
    public static final double USD_TO_BYN_RATE = 0.4;
    public static final double EUR_TO_USD_RATE = 0.9;
    public static final double USD_TO_EUR_RATE = 1.11;
    private static final int MAXIMUM_QUEUED_TRANSACTIONS = 6;

    private static StockExchangePlatform INSTANCE;

    private final Semaphore semaphore = new Semaphore(MAXIMUM_QUEUED_TRANSACTIONS);
    private final Lock lock = new ReentrantLock();
    private final Set<Participant> participants = new HashSet<>();

    private StockExchangePlatform() {
    }

    public boolean registerParticipant(Participant participant) {
        return participants.add(participant);
    }

    public int participantsNumber() {
        return participants.size();
    }

    public void validateAndPerformTransaction(Participant firstParticipant, int numberOfSecondParticipant, TransactionType transactionType, double amountToGive, double amountToReceive) throws InterruptedException, TransactionException {
        semaphore.acquire();
        lock.lock();
        try {
            Participant secondParticipant = (new ArrayList<>(participants)).get(numberOfSecondParticipant);
            if (firstParticipant.equals(secondParticipant)) {
                throw new TransactionException("Cannot perform a transaction with yourself");
            }
            validateTransaction(firstParticipant, secondParticipant, transactionType, amountToGive, amountToReceive);
            performTransaction(firstParticipant, secondParticipant, transactionType, amountToGive, amountToReceive);
        } finally {
            lock.unlock();
            semaphore.release();
        }
    }

    private void performTransaction(Participant firstParticipant, Participant secondParticipant, TransactionType transactionType, double amountToGive, double amountToReceive) {
        BigDecimal formattedAmountToGive = new BigDecimal(amountToGive).setScale(2, RoundingMode.DOWN);
        amountToGive = formattedAmountToGive.doubleValue();
        BigDecimal formattedAmountToReceive = new BigDecimal(amountToReceive).setScale(2, RoundingMode.DOWN);
        amountToReceive = formattedAmountToReceive.doubleValue();
        double currentFirstParticipantBank;
        double currentSecondParticipantBank;
        switch (transactionType) {
            case USD_TO_BYN:
                currentFirstParticipantBank = firstParticipant.getUsd();
                firstParticipant.setUsd(currentFirstParticipantBank - amountToGive);
                firstParticipant.setByn(currentFirstParticipantBank + amountToReceive);
                currentSecondParticipantBank = secondParticipant.getByn();
                secondParticipant.setUsd(currentSecondParticipantBank + amountToGive);
                secondParticipant.setByn(currentSecondParticipantBank - amountToReceive);
                break;
            case USD_TO_EUR:
                currentFirstParticipantBank = firstParticipant.getUsd();
                firstParticipant.setUsd(currentFirstParticipantBank - amountToGive);
                firstParticipant.setEur(currentFirstParticipantBank + amountToReceive);
                currentSecondParticipantBank = secondParticipant.getEur();
                secondParticipant.setUsd(currentSecondParticipantBank + amountToGive);
                secondParticipant.setEur(currentSecondParticipantBank - amountToReceive);
                break;
            case BYN_TO_EUR:
                currentFirstParticipantBank = firstParticipant.getByn();
                firstParticipant.setByn(currentFirstParticipantBank - amountToGive);
                firstParticipant.setEur(currentFirstParticipantBank + amountToReceive);
                currentSecondParticipantBank = secondParticipant.getEur();
                secondParticipant.setByn(currentSecondParticipantBank + amountToGive);
                secondParticipant.setEur(currentSecondParticipantBank - amountToReceive);
                break;
            case BYN_TO_USD:
                currentFirstParticipantBank = firstParticipant.getByn();
                firstParticipant.setByn(currentFirstParticipantBank - amountToGive);
                firstParticipant.setUsd(currentFirstParticipantBank + amountToReceive);
                currentSecondParticipantBank = secondParticipant.getUsd();
                secondParticipant.setByn(currentSecondParticipantBank + amountToGive);
                secondParticipant.setUsd(currentSecondParticipantBank - amountToReceive);
                break;
            case EUR_TO_BYN:
                currentFirstParticipantBank = firstParticipant.getEur();
                firstParticipant.setEur(currentFirstParticipantBank - amountToGive);
                firstParticipant.setByn(currentFirstParticipantBank + amountToReceive);
                currentSecondParticipantBank = secondParticipant.getByn();
                secondParticipant.setEur(currentSecondParticipantBank + amountToGive);
                secondParticipant.setByn(currentSecondParticipantBank - amountToReceive);
                break;
            case EUR_TO_USD:
                currentFirstParticipantBank = firstParticipant.getEur();
                firstParticipant.setEur(currentFirstParticipantBank - amountToGive);
                firstParticipant.setUsd(currentFirstParticipantBank + amountToReceive);
                currentSecondParticipantBank = secondParticipant.getUsd();
                secondParticipant.setEur(currentSecondParticipantBank + amountToGive);
                secondParticipant.setUsd(currentSecondParticipantBank - amountToReceive);
                break;
        }
    }

    private void validateTransaction(Participant firstParticipant, Participant secondParticipant, TransactionType transactionType, double amountToGive, double amountToReceive) throws TransactionException {
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

    public static StockExchangePlatform getInstance() {
        Lock firstLock = new ReentrantLock();
        Lock secondLock = new ReentrantLock();
        firstLock.lock();
        try {
            if (INSTANCE == null) {
                secondLock.lock();
                try {
                    if (INSTANCE == null) {
                        INSTANCE = new StockExchangePlatform();
                    }
                } finally {
                    secondLock.unlock();
                }
            }
        } finally {
            firstLock.unlock();
        }
        return INSTANCE;
    }
}
