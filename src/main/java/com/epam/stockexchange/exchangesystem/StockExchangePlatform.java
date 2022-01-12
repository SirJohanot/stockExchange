package com.epam.stockexchange.exchangesystem;

import com.epam.stockexchange.entity.Participant;
import com.epam.stockexchange.exception.TransactionException;

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
    private final TransactionPerformer transactionPerformer = new TransactionPerformer();
    private final TransactionValidator transactionValidator = new TransactionValidator();

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
            transactionValidator.validateTransaction(firstParticipant, secondParticipant, transactionType, amountToGive, amountToReceive);
            transactionPerformer.performTransaction(firstParticipant, secondParticipant, transactionType, amountToGive, amountToReceive);
        } finally {
            lock.unlock();
            semaphore.release();
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
