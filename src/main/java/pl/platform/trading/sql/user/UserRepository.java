package pl.platform.trading.sql.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.platform.trading.sql.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUid(Long uid);

    public User findByEmail(String email);

}
