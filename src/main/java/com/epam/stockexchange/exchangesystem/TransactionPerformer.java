package com.epam.stockexchange.exchangesystem;

import com.epam.stockexchange.entity.Participant;

import java.math.BigDecimal;

public class TransactionPerformer {

    public void performTransaction(Participant firstParticipant, Participant secondParticipant, TransactionType transactionType, BigDecimal amountToGive, BigDecimal amountToReceive) {
        switch (transactionType) {
            case USD_TO_BYN:
                firstParticipant.setUsd(firstParticipant.getUsd().subtract(amountToGive));
                firstParticipant.setByn(firstParticipant.getByn().add(amountToReceive));
                secondParticipant.setUsd(secondParticipant.getUsd().add(amountToGive));
                secondParticipant.setByn(secondParticipant.getByn().subtract(amountToReceive));
                break;
            case USD_TO_EUR:
                firstParticipant.setUsd(firstParticipant.getUsd().subtract(amountToGive));
                firstParticipant.setEur(firstParticipant.getEur().add(amountToReceive));
                secondParticipant.setUsd(secondParticipant.getUsd().add(amountToGive));
                secondParticipant.setEur(secondParticipant.getEur().subtract(amountToReceive));
                break;
            case BYN_TO_EUR:
                firstParticipant.setByn(firstParticipant.getByn().subtract(amountToGive));
                firstParticipant.setEur(firstParticipant.getEur().add(amountToReceive));
                secondParticipant.setByn(secondParticipant.getByn().add(amountToGive));
                secondParticipant.setEur(secondParticipant.getEur().subtract(amountToReceive));
                break;
            case BYN_TO_USD:
                firstParticipant.setByn(firstParticipant.getByn().subtract(amountToGive));
                firstParticipant.setUsd(firstParticipant.getUsd().add(amountToReceive));
                secondParticipant.setByn(secondParticipant.getByn().add(amountToGive));
                secondParticipant.setUsd(secondParticipant.getUsd().subtract(amountToReceive));
                break;
            case EUR_TO_BYN:
                firstParticipant.setEur(firstParticipant.getEur().subtract(amountToGive));
                firstParticipant.setByn(firstParticipant.getByn().add(amountToReceive));
                secondParticipant.setEur(secondParticipant.getEur().add(amountToGive));
                secondParticipant.setByn(secondParticipant.getByn().subtract(amountToReceive));
                break;
            case EUR_TO_USD:
                firstParticipant.setEur(firstParticipant.getEur().subtract(amountToGive));
                firstParticipant.setUsd(firstParticipant.getUsd().add(amountToReceive));
                secondParticipant.setEur(secondParticipant.getEur().add(amountToGive));
                secondParticipant.setUsd(secondParticipant.getUsd().subtract(amountToReceive));
                break;
        }
    }

}
