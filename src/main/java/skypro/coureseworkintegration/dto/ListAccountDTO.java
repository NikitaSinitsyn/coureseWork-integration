package skypro.coureseworkintegration.dto;


import skypro.coureseworkintegration.entity.Account;
import skypro.coureseworkintegration.entity.AccountCurrency;

public class ListAccountDTO {
    private final Long accountId;
    private final AccountCurrency currency;

    public ListAccountDTO(Long accountId, AccountCurrency currency) {
        this.accountId = accountId;
        this.currency = currency;
    }

    public Long getAccountId() {
        return accountId;
    }

    public AccountCurrency getCurrency() {
        return currency;
    }

    public static ListAccountDTO from(Account account) {
        return new ListAccountDTO(account.getId(), account.getAccountCurrency());
    }
}