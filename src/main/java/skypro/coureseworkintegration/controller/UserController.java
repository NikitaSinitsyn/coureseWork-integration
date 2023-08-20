package skypro.coureseworkintegration.controller;


import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import skypro.coureseworkintegration.dto.BankingUserDetails;
import skypro.coureseworkintegration.dto.CreateUserRequest;
import skypro.coureseworkintegration.dto.ListUserDTO;
import skypro.coureseworkintegration.dto.UserDTO;
import skypro.coureseworkintegration.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO createUser(@RequestBody @Valid CreateUserRequest userRequest) {
        return userService.createUser(userRequest.getUsername(), userRequest.getPassword(), userRequest.getRole());
    }
    @GetMapping("/list")
    public List<ListUserDTO> getAllUsers(){
        return userService.listUsers();
    }
    @GetMapping("/me")
    public UserDTO getMyProfile(Authentication authentication){
        BankingUserDetails bankingUserDetails = (BankingUserDetails) authentication.getPrincipal();
        return userService.getUser(bankingUserDetails.getId());
    }
}