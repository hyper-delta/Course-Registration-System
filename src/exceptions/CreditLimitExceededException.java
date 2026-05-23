package exceptions;

public class CreditLimitExceededException extends CRSException {
    public CreditLimitExceededException(int current, int adding, int max) {
        super(String.format(
            "Credit limit exceeded: %d (current) + %d (new) = %d > %d (max)",
            current, adding, current + adding, max));
    }
}