package skypro.coureseworkintegration.service;


import java.util.ArrayList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import skypro.coureseworkintegration.dto.AccountDTO;
import skypro.coureseworkintegration.entity.Account;
import skypro.coureseworkintegration.entity.AccountCurrency;
import skypro.coureseworkintegration.entity.User;
import skypro.coureseworkintegration.exception.AccountNotFoundException;
import skypro.coureseworkintegration.exception.InsufficientFundsException;
import skypro.coureseworkintegration.exception.InvalidAmountException;
import skypro.coureseworkintegration.exception.WrongCurrencyException;
import skypro.coureseworkintegration.repository.AccountRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void createDefaultAccounts(User user) {
        user.setAccounts(new ArrayList<>());
        for (AccountCurrency currency : AccountCurrency.values()) {
            Account account = new Account();
            account.setUser(user);
            account.setAccountCurrency(currency);
            account.setAmount(1L);
            user.getAccounts().add(account);
            accountRepository.save(account);
        }
    }

    @Transactional(readOnly = true)
    public AccountDTO getAccount(long userId, Long accountId) {
        return accountRepository
                .getAccountByUser_IdAndId(userId, accountId)
                .map(AccountDTO::from)
                .orElseThrow(AccountNotFoundException::new);
    }

    @Transactional
    public void validateCurrency(long sourceAccount, long destinationAccount) {
        Account acc1 =
                accountRepository.findById(sourceAccount).orElseThrow(AccountNotFoundException::new);
        Account acc2 =
                accountRepository.findById(destinationAccount).orElseThrow(AccountNotFoundException::new);
        if (!acc1.getAccountCurrency().equals(acc2.getAccountCurrency())){
            throw new WrongCurrencyException();
        }
    }

    @Transactional
    public AccountDTO depositToAccount(long userId, Long accountId, long amount) {
        if (amount < 0) {
            throw new InvalidAmountException();
        }
        Account account =
                accountRepository
                        .getAccountByUser_IdAndId(userId, accountId)
                        .orElseThrow(AccountNotFoundException::new);
        account.setAmount(account.getAmount() + amount);
        return AccountDTO.from(account);
    }

    @Transactional
    public AccountDTO withdrawFromAccount(long id, Long accountId, long amount) {
        if (amount < 0) {
            throw new InvalidAmountException();
        }
        Account account =
                accountRepository
                        .getAccountByUser_IdAndId(id, accountId)
                        .orElseThrow(AccountNotFoundException::new);
        if (account.getAmount() < amount) {
            throw new InsufficientFundsException(
                    "Cannot withdraw " + amount + " " + account.getAccountCurrency().name());
        }
        account.setAmount(account.getAmount() - amount);
        return AccountDTO.from(account);
    }
}
