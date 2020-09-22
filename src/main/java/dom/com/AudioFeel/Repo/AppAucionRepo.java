package dom.com.AudioFeel.Repo;

import dom.com.AudioFeel.model.AppAuction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppAucionRepo extends JpaRepository<AppAuction,Long> {


    List<AppAuction>findByOwner(String owner);

}
