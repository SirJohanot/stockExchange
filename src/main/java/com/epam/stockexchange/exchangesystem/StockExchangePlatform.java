package com.epam.stockexchange.exchangesystem;

import com.epam.stockexchange.entity.Participant;
import com.epam.stockexchange.exception.TransactionException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StockExchangePlatform {

    public static final BigDecimal BYN_TO_EUR_RATE = BigDecimal.valueOf(2.75);
    public static final BigDecimal EUR_TO_BYN_RATE = BigDecimal.valueOf(0.36);
    public static final BigDecimal BYN_TO_USD_RATE = BigDecimal.valueOf(2.5);
    public static final BigDecimal USD_TO_BYN_RATE = BigDecimal.valueOf(0.4);
    public static final BigDecimal EUR_TO_USD_RATE = BigDecimal.valueOf(0.9);
    public static final BigDecimal USD_TO_EUR_RATE = BigDecimal.valueOf(1.11);
    private static final int MAXIMUM_QUEUED_TRANSACTIONS = 6;
    private static final Lock lock = new ReentrantLock();

    private static StockExchangePlatform INSTANCE;

    private final Semaphore semaphore = new Semaphore(MAXIMUM_QUEUED_TRANSACTIONS);
    private final Set<Participant> participants = new HashSet<>();
    private final TransactionPerformer transactionPerformer = new TransactionPerformer();
    private final TransactionValidator transactionValidator = new TransactionValidator();

    private StockExchangePlatform() {
    }

    public boolean registerParticipant(Participant participant) {
        lock.lock();
        try {
            return participants.add(participant);
        } finally {
            lock.unlock();
        }
    }

    public int getParticipantsNumber() {
        lock.lock();
        try {
            return participants.size();
        } finally {
            lock.unlock();
        }
    }

    public void validateAndPerformTransaction(Participant firstParticipant, int numberOfSecondParticipant, TransactionType transactionType, BigDecimal amountToGive, BigDecimal amountToReceive) throws InterruptedException, TransactionException {
        semaphore.acquire();
        lock.lock();
        try {
            Participant secondParticipant = (new ArrayList<>(participants)).get(numberOfSecondParticipant);
            transactionValidator.validateTransaction(firstParticipant, secondParticipant, transactionType, amountToGive, amountToReceive);
            transactionPerformer.performTransaction(firstParticipant, secondParticipant, transactionType, amountToGive, amountToReceive);
        } finally {
            lock.unlock();
            semaphore.release();
        }
    }

    public static StockExchangePlatform getInstance() {
        StockExchangePlatform localInstance = INSTANCE;
        if (localInstance == null) {
            lock.lock();
            try {
                localInstance = INSTANCE;
                if (localInstance == null) {
                    INSTANCE = localInstance = new StockExchangePlatform();
                }
            } finally {
                lock.unlock();
            }
        }
        return localInstance;
    }
}
