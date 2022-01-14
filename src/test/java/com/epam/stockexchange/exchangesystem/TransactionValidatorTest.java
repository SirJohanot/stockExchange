package com.epam.stockexchange.exchangesystem;

import com.epam.stockexchange.entity.Participant;
import com.epam.stockexchange.exception.TransactionException;
import org.junit.Test;

import java.math.BigDecimal;

public class TransactionValidatorTest {

    private static final TransactionValidator TRANSACTION_VALIDATOR = new TransactionValidator();

    private static final Participant FIRST_PARTICIPANT = new Participant(1, BigDecimal.valueOf(2000.50), BigDecimal.valueOf(1000.75), BigDecimal.valueOf(500.00));
    private static final Participant SECOND_PARTICIPANT = new Participant(2, BigDecimal.valueOf(2000.50), BigDecimal.valueOf(1000.75), BigDecimal.valueOf(500.00));

    @Test
    public void testValidateTransactionShouldReturnTrueForAValidTransaction() throws TransactionException {
        //given
        TransactionType transactionType = TransactionType.BYN_TO_EUR;
        BigDecimal amountToGive = BigDecimal.valueOf(450.75);
        BigDecimal amountToReceive = BigDecimal.valueOf(1500.20);
        //when
        TRANSACTION_VALIDATOR.validateTransaction(FIRST_PARTICIPANT, SECOND_PARTICIPANT, transactionType, amountToGive, amountToReceive);
        //then
    }

    @Test(expected = TransactionException.class)
    public void testValidateTransactionShouldThrowATransactionExceptionWhenTwoParticipantsAreIdentical() throws TransactionException {
        //given
        TransactionType transactionType = TransactionType.BYN_TO_EUR;
        BigDecimal amountToGive = BigDecimal.valueOf(450.75);
        BigDecimal amountToReceive = BigDecimal.valueOf(1500.20);
        //when
        TRANSACTION_VALIDATOR.validateTransaction(FIRST_PARTICIPANT, FIRST_PARTICIPANT, transactionType, amountToGive, amountToReceive);
        //then
    }

    @Test(expected = TransactionException.class)
    public void testValidateTransactionShouldThrowATransactionExceptionWhenFirstParticipantDoesNotHaveEnoughCurrencyToGive() throws TransactionException {
        //given
        TransactionType transactionType = TransactionType.BYN_TO_EUR;
        BigDecimal amountToGive = BigDecimal.valueOf(900.11);
        BigDecimal amountToReceive = BigDecimal.valueOf(1500.20);
        //when
        TRANSACTION_VALIDATOR.validateTransaction(FIRST_PARTICIPANT, FIRST_PARTICIPANT, transactionType, amountToGive, amountToReceive);
        //then
    }

    @Test(expected = TransactionException.class)
    public void testValidateTransactionShouldThrowATransactionExceptionWhenSecondParticipantDoesNotHaveEnoughCurrencyToGive() throws TransactionException {
        //given
        TransactionType transactionType = TransactionType.BYN_TO_EUR;
        BigDecimal amountToGive = BigDecimal.valueOf(450.75);
        BigDecimal amountToReceive = BigDecimal.valueOf(2100.26);
        //when
        TRANSACTION_VALIDATOR.validateTransaction(FIRST_PARTICIPANT, FIRST_PARTICIPANT, transactionType, amountToGive, amountToReceive);
        //then
    }

    @Test(expected = TransactionException.class)
    public void testValidateTransactionShouldThrowATransactionExceptionWhenANegativeAmountIsPassed() throws TransactionException {
        //given
        TransactionType transactionType = TransactionType.BYN_TO_EUR;
        BigDecimal amountToGive = BigDecimal.valueOf(-450.75);
        BigDecimal amountToReceive = BigDecimal.valueOf(2100.26);
        //when
        TRANSACTION_VALIDATOR.validateTransaction(FIRST_PARTICIPANT, FIRST_PARTICIPANT, transactionType, amountToGive, amountToReceive);
        //then
    }
}
