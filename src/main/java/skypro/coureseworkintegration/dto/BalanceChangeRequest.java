package skypro.coureseworkintegration.dto;

import org.hibernate.validator.constraints.Range;

public class BalanceChangeRequest {
    private long amount;

    public BalanceChangeRequest(long depositAmount) {

    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}