package dom.com.AudioFeel;


import com.sun.istack.NotNull;
import dom.com.AudioFeel.Repo.AppAucionRepo;
import dom.com.AudioFeel.model.AppAuction;
import dom.com.AudioFeel.model.AppUser;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class AuctionService {

    private AppAucionRepo appAucionRepo;

    public AuctionService(AppAucionRepo appAucionRepo) {
        this.appAucionRepo = appAucionRepo;
    }

    public void addAuction(AppAuction appAuction, AppUser appUser){

        appAuction.setDate(LocalDate.now(ZoneId.of("Europe/Warsaw")));
        appAuction.setTime(LocalTime.now(ZoneId.of("Europe/Warsaw")) );
        appAucionRepo.save(appAuction);

    }
    public void changePrice(AppAuction appAuction , int a){

        appAuction.setPrice(a);
        appAucionRepo.save(appAuction);

    }
}
