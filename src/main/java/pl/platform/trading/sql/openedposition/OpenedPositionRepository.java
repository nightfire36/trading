package pl.platform.trading.sql.openedposition;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.platform.trading.sql.openedposition.OpenedPosition;

@Repository
public interface OpenedPositionRepository extends JpaRepository<OpenedPosition, Long> {

    public OpenedPosition findByTid(Long tid);

    public List<OpenedPosition> findByUid(Long uid);

    public Long deleteByTid(Long tid);

}
