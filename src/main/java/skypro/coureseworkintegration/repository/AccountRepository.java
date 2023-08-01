package skypro.coureseworkintegration.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import skypro.coureseworkintegration.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> getAccountByUser_IdAndId(Long userId, Long accountId);
}