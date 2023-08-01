package skypro.coureseworkintegration.service;


import java.util.ArrayList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import skypro.coureseworkintegration.dto.AccountDTO;
import skypro.coureseworkintegration.entity.Account;
import skypro.coureseworkintegration.entity.AccountCurrency;
import skypro.coureseworkintegration.entity.User;
import skypro.coureseworkintegration.exception.*;
import skypro.coureseworkintegration.repository.AccountRepository;
import skypro.coureseworkintegration.repository.UserRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
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
        Account acc1 = accountRepository.findById(sourceAccount).orElseThrow(AccountNotFoundException::new);
        Account acc2 = accountRepository.findById(destinationAccount).orElseThrow(AccountNotFoundException::new);
        if (!acc1.getAccountCurrency().equals(acc2.getAccountCurrency())) {
            throw new WrongCurrencyException();
        }
    }

    @Transactional
    public AccountDTO depositToAccount(long userId, Long accountId, long amount) {
        if (amount < 0) {
            throw new InvalidAmountException();
        }
        Account account = accountRepository
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
        Account account = accountRepository
                .getAccountByUser_IdAndId(id, accountId)
                .orElseThrow(AccountNotFoundException::new);
        if (account.getAmount() < amount) {
            throw new InsufficientFundsException("Cannot withdraw " + amount + " " + account.getAccountCurrency().name());
        }
        account.setAmount(account.getAmount() - amount);
        return AccountDTO.from(account);
    }

    @Transactional
    public void transferBetweenAccounts(long userId, Long sourceAccountId, Long destinationAccountId, long amount) {
        if (amount < 0) {
            throw new InvalidAmountException();
        }
        Account sourceAccount = getAccountByUserIdAndAccountId(userId, sourceAccountId);
        Account destinationAccount = getAccountByAccountId(destinationAccountId);

        if (!sourceAccount.getAccountCurrency().equals(destinationAccount.getAccountCurrency())) {
            throw new WrongCurrencyException();
        }

        if (sourceAccount.getAmount() < amount) {
            throw new InsufficientFundsException("Cannot transfer " + amount + " " + sourceAccount.getAccountCurrency().name() + " from account " + sourceAccountId);
        }

        sourceAccount.setAmount(sourceAccount.getAmount() - amount);
        destinationAccount.setAmount(destinationAccount.getAmount() + amount);
    }

    @Transactional
    public AccountDTO createAccount(Long userId, long initialBalance) {
        Account account = new Account();
        account.setUser(userRepository.findById(userId).orElseThrow(UserNotFoundException::new));
        account.setAmount(initialBalance);
        account.setAccountCurrency(AccountCurrency.USD); // Set the currency you want here
        accountRepository.save(account);
        return AccountDTO.from(account);
    }

    @Transactional
    public void changeAccountCurrency(Long accountId, AccountCurrency newCurrency) {
        Account account = accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
        account.setAccountCurrency(newCurrency);
    }
    private Account getAccountByUserIdAndAccountId(long userId, Long accountId) {
        return accountRepository.getAccountByUser_IdAndId(userId, accountId).orElseThrow(AccountNotFoundException::new);
    }

    private Account getAccountByAccountId(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
    }

}
