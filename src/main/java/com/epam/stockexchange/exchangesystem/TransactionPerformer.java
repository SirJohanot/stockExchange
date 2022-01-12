package com.epam.stockexchange.exchangesystem;

import com.epam.stockexchange.entity.Participant;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TransactionPerformer {

    public void performTransaction(Participant firstParticipant, Participant secondParticipant, TransactionType transactionType, double amountToGive, double amountToReceive) {
        BigDecimal formattedAmountToGive = new BigDecimal(amountToGive).setScale(2, RoundingMode.DOWN);
        amountToGive = formattedAmountToGive.doubleValue();
        BigDecimal formattedAmountToReceive = new BigDecimal(amountToReceive).setScale(2, RoundingMode.DOWN);
        amountToReceive = formattedAmountToReceive.doubleValue();
        double currentFirstParticipantBank;
        double currentSecondParticipantBank;
        switch (transactionType) {
            case USD_TO_BYN:
                currentFirstParticipantBank = firstParticipant.getUsd();
                firstParticipant.setUsd(Double.sum(currentFirstParticipantBank, -amountToGive));
                firstParticipant.setByn(Double.sum(currentFirstParticipantBank, amountToReceive));
                currentSecondParticipantBank = secondParticipant.getByn();
                secondParticipant.setUsd(Double.sum(currentSecondParticipantBank, amountToGive));
                secondParticipant.setByn(Double.sum(currentSecondParticipantBank, -amountToReceive));
                break;
            case USD_TO_EUR:
                currentFirstParticipantBank = firstParticipant.getUsd();
                firstParticipant.setUsd(Double.sum(currentFirstParticipantBank, -amountToGive));
                firstParticipant.setEur(Double.sum(currentFirstParticipantBank, amountToReceive));
                currentSecondParticipantBank = secondParticipant.getEur();
                secondParticipant.setUsd(Double.sum(currentSecondParticipantBank, amountToGive));
                secondParticipant.setEur(Double.sum(currentSecondParticipantBank, -amountToReceive));
                break;
            case BYN_TO_EUR:
                currentFirstParticipantBank = firstParticipant.getByn();
                firstParticipant.setByn(Double.sum(currentFirstParticipantBank, -amountToGive));
                firstParticipant.setEur(Double.sum(currentFirstParticipantBank, amountToReceive));
                currentSecondParticipantBank = secondParticipant.getEur();
                secondParticipant.setByn(Double.sum(currentSecondParticipantBank, amountToGive));
                secondParticipant.setEur(Double.sum(currentSecondParticipantBank, -amountToReceive));
                break;
            case BYN_TO_USD:
                currentFirstParticipantBank = firstParticipant.getByn();
                firstParticipant.setByn(Double.sum(currentFirstParticipantBank, -amountToGive));
                firstParticipant.setUsd(Double.sum(currentFirstParticipantBank, amountToReceive));
                currentSecondParticipantBank = secondParticipant.getUsd();
                secondParticipant.setByn(Double.sum(currentSecondParticipantBank, amountToGive));
                secondParticipant.setUsd(Double.sum(currentSecondParticipantBank, -amountToReceive));
                break;
            case EUR_TO_BYN:
                currentFirstParticipantBank = firstParticipant.getEur();
                firstParticipant.setEur(Double.sum(currentFirstParticipantBank, -amountToGive));
                firstParticipant.setByn(Double.sum(currentFirstParticipantBank, amountToReceive));
                currentSecondParticipantBank = secondParticipant.getByn();
                secondParticipant.setEur(Double.sum(currentSecondParticipantBank, amountToGive));
                secondParticipant.setByn(Double.sum(currentSecondParticipantBank, -amountToReceive));
                break;
            case EUR_TO_USD:
                currentFirstParticipantBank = firstParticipant.getEur();
                firstParticipant.setEur(Double.sum(currentFirstParticipantBank, -amountToGive));
                firstParticipant.setUsd(Double.sum(currentFirstParticipantBank, amountToReceive));
                currentSecondParticipantBank = secondParticipant.getUsd();
                secondParticipant.setEur(Double.sum(currentSecondParticipantBank, amountToGive));
                secondParticipant.setUsd(Double.sum(currentSecondParticipantBank, -amountToReceive));
                break;
        }
    }

}
