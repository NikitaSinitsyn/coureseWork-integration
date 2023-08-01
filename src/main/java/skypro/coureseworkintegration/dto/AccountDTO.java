package skypro.coureseworkintegration.dto;


import skypro.coureseworkintegration.entity.Account;
import skypro.coureseworkintegration.entity.AccountCurrency;

public class AccountDTO {
    private final long id;
    private final long amount;
    private final AccountCurrency currency;

    public AccountDTO(long id, long amount, AccountCurrency currency) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
    }

    public long getId() {
        return id;
    }

    public long getAmount() {
        return amount;
    }

    public AccountCurrency getCurrency() {
        return currency;
    }

    public static AccountDTO from(Account account) {
        return new AccountDTO(account.getId(), account.getAmount(), account.getAccountCurrency());
    }
}