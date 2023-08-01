package skypro.coureseworkintegration.controller;


import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import skypro.coureseworkintegration.dto.AccountDTO;
import skypro.coureseworkintegration.dto.BalanceChangeRequest;
import skypro.coureseworkintegration.dto.BankingUserDetails;
import skypro.coureseworkintegration.service.AccountService;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public AccountDTO getUserAccount(Authentication authentication, @PathVariable("id") Long accountId) {
        BankingUserDetails bankingUserDetails = (BankingUserDetails) authentication.getPrincipal();
        return accountService.getAccount(bankingUserDetails.getId(), accountId);
    }

    @PostMapping("/deposit/{id}")
    public AccountDTO depositToAccount(Authentication authentication,
                                       @PathVariable("id") Long accountId,
                                       @RequestBody BalanceChangeRequest balanceChangeRequest){
        BankingUserDetails bankingUserDetails = (BankingUserDetails) authentication.getPrincipal();
        return accountService.depositToAccount(bankingUserDetails.getId(),accountId, balanceChangeRequest.getAmount());
    }

    @PostMapping("/withdraw/{id}")
    public AccountDTO withdrawFromAccount(Authentication authentication,
                                          @PathVariable("id") Long accountId,
                                          @RequestBody BalanceChangeRequest balanceChangeRequest){
        BankingUserDetails bankingUserDetails = (BankingUserDetails) authentication.getPrincipal();
        return accountService.withdrawFromAccount(bankingUserDetails.getId(),accountId, balanceChangeRequest.getAmount());
    }
}