package com.epam.stockexchange.entity;

import com.epam.stockexchange.exception.TransactionException;
import com.epam.stockexchange.exchangesystem.StockExchangePlatform;
import com.epam.stockexchange.exchangesystem.TransactionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class Participant implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(Participant.class);
    private static final Random decisionMaker = new Random();
    private static final double MINIMUM_TRANSACTION_SIZE = 0.01;

    private int id;
    private double eur;
    private double usd;
    private double byn;

    public Participant() {
    }

    public Participant(int id, double eur, double usd, double byn) {
        this.id = id;
        this.eur = eur;
        this.usd = usd;
        this.byn = byn;
    }

    @Override
    public void run() {
        StockExchangePlatform exchangePlatform = StockExchangePlatform.getInstance();
        exchangePlatform.registerParticipant(this);
        while (true) {
            TransactionType transactionToMake = TransactionType.values()[decisionMaker
                    .nextInt(TransactionType.values()
                            .length)];
            double maximumAvailableOfCurrency;
            switch (transactionToMake) {
                case BYN_TO_EUR:
                case BYN_TO_USD:
                    maximumAvailableOfCurrency = byn;
                    break;
                case EUR_TO_BYN:
                case EUR_TO_USD:
                    maximumAvailableOfCurrency = eur;
                    break;
                case USD_TO_BYN:
                case USD_TO_EUR:
                    maximumAvailableOfCurrency = usd;
                    break;
                default:
                    maximumAvailableOfCurrency = 0;
                    break;
            }
            double amountToGive = MINIMUM_TRANSACTION_SIZE + (maximumAvailableOfCurrency - MINIMUM_TRANSACTION_SIZE) * decisionMaker.nextDouble();
            double amountToReceive = amountToGive;
            switch (transactionToMake) {
                case BYN_TO_EUR:
                    amountToReceive /= StockExchangePlatform.BYN_TO_EUR_RATE;
                    break;
                case USD_TO_BYN:
                    amountToReceive /= StockExchangePlatform.USD_TO_BYN_RATE;
                    break;
                case BYN_TO_USD:
                    amountToReceive /= StockExchangePlatform.BYN_TO_USD_RATE;
                    break;
                case USD_TO_EUR:
                    amountToReceive /= StockExchangePlatform.USD_TO_EUR_RATE;
                    break;
                case EUR_TO_BYN:
                    amountToReceive /= StockExchangePlatform.EUR_TO_BYN_RATE;
                    break;
                case EUR_TO_USD:
                    amountToReceive /= StockExchangePlatform.EUR_TO_USD_RATE;
                    break;
                default:
                    amountToReceive = 0;
                    break;
            }
            int numberOfParticipants = exchangePlatform.participantsNumber();
            int participantToExchangeWith = decisionMaker.nextInt(numberOfParticipants);
            try {
                exchangePlatform.validateAndPerformTransaction(this, participantToExchangeWith, transactionToMake, amountToGive, amountToReceive);
            } catch (InterruptedException | TransactionException e) {
                LOGGER.error(e);
            }
        }
    }

    public boolean transactionQuery() {
        return decisionMaker.nextBoolean();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getEur() {
        return eur;
    }

    public void setEur(double eur) {
        this.eur = eur;
    }

    public double getUsd() {
        return usd;
    }

    public void setUsd(double usd) {
        this.usd = usd;
    }

    public double getByn() {
        return byn;
    }

    public void setByn(double byn) {
        this.byn = byn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Participant that = (Participant) o;
        if (id != that.id) {
            return false;
        }
        if (Double.compare(that.eur, eur) != 0) {
            return false;
        }
        if (Double.compare(that.usd, usd) != 0) {
            return false;
        }
        return Double.compare(that.byn, byn) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        temp = Double.doubleToLongBits(eur);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(usd);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(byn);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", eur=" + eur +
                ", usd=" + usd +
                ", byn=" + byn +
                '}';
    }
}
