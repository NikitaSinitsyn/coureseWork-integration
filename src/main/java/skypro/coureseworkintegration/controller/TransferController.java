package skypro.coureseworkintegration.controller;


import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import skypro.coureseworkintegration.dto.BankingUserDetails;
import skypro.coureseworkintegration.dto.TransferRequest;
import skypro.coureseworkintegration.service.TransferService;

@RestController
@RequestMapping("/transfer")
public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public void transfer(
            Authentication authentication, @RequestBody TransferRequest transferRequest) {
        BankingUserDetails bankingUserDetails = (BankingUserDetails) authentication.getPrincipal();
        transferService.transfer(bankingUserDetails.getId(), transferRequest);
    }
}