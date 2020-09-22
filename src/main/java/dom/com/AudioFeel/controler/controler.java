package dom.com.AudioFeel.controler;

import dom.com.AudioFeel.AuctionService;
import dom.com.AudioFeel.Repo.AppAucionRepo;
import dom.com.AudioFeel.Repo.AppUserRepo;
import dom.com.AudioFeel.UserService;
import dom.com.AudioFeel.model.AppAuction;
import dom.com.AudioFeel.model.AppUser;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Controller
public class controler {

    private UserService userService;
    private AuctionService auctionService;
    private AppAucionRepo appAucionRepo;
    private AppUserRepo appUserRepo;

    public controler(UserService userService, AuctionService auctionService, AppAucionRepo appAucionRepo,AppUserRepo appUserRepo) {
        this.userService = userService;
        this.auctionService =auctionService;
        this.appAucionRepo = appAucionRepo;
        this.appUserRepo = appUserRepo;
    }

    @GetMapping("/main")
    public String getMain(Model model) {

        List<AppAuction> allAuctions = appAucionRepo.findAll();
        model.addAttribute("allAuctions",allAuctions);

        return "index";
    }

   @GetMapping("/one/{id}")
    public String getAuction(@PathVariable Long id, Model model) {

        List<AppAuction> allAuc = appAucionRepo.findAll();

        Optional<AppAuction> oneAuctionOpt = appAucionRepo.findById(id);
        AppAuction oneAuction = oneAuctionOpt.get();

        model.addAttribute("allAuc",allAuc);
        model.addAttribute("oneAuction",oneAuction);

        return "oneAuction";
    }

    @GetMapping("/kontakt")
    public String getKontat() {
        return "kontakt";
    }

    @GetMapping("/trial")
    public String getTaial() {
        return "trial";
    }

    @GetMapping("/panel")
    public String getUser() {
        return "panelUserNiewiem";
    }

    @GetMapping("/user")
    public String getPanelUser(Principal principal,Model model) {
        List<AppAuction> auctions = appAucionRepo.findAll();
        List<AppAuction> ownerList = appAucionRepo.findByOwner(principal.getName());

        model.addAttribute("auctions",auctions);
        model.addAttribute("ownerList",ownerList);

        model.addAttribute("username", principal.getName());

        return "panelUser";
    }

    @PostMapping("/user")
    public String postLogowanie(@ModelAttribute AppUser appUser ){
        return "panelUser";
    }

    @GetMapping("/admin")
    public String getPanelAdmin(@RequestParam(defaultValue = "0")int page, Principal principal, Model model) {


        List<AppAuction> auctionsAdmin = appAucionRepo.findAll();
        List<AppAuction> ownerAdminList = appAucionRepo.findByOwner(principal.getName());

        
        model.addAttribute("auctionsAdmin",appAucionRepo.findAll(PageRequest.of(page,4)));

        model.addAttribute("username", principal.getName());
        return "panelAdmin";
    }

    @GetMapping("/article")
    public String getArticle() {
        return "article";
    }

    @GetMapping("/audio")
    public String getAudio(Model model) {

        model.addAttribute("AppAuction", new AppAuction());

        return "newAuction";
    }
    @PostMapping("/audio")
    public String toAudio(@ModelAttribute AppAuction appAuction,AppUser appUser,Principal principal, Model model,
                          @RequestParam("fielImgae") MultipartFile multipartFile) throws IOException {

        model.addAttribute("username",principal.getName());

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        appAuction.setImage(fileName);

        appAuction.setOwner(principal.getName());
        auctionService.addAuction(appAuction,appUser);

        return "redirect:/user";

    }

    @GetMapping("/logowanie")
    public String getLogowanie(Model model) {
        //model.addAttribute("listaUser", User.listaUser);
        model.addAttribute("konto", new AppUser());

        return "logowanie";
    }
    @RequestMapping(value="/logout", method=RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/main";
    }

        @PostMapping("/logowanie")
    public String toLogowanie(@ModelAttribute AppUser appUser ){
        System.out.println(appUser.getUsername());
         return "redirect:/user";
    }


    @GetMapping("/rejestracja")
    public String getRejestracja(Model model) {
        model.addAttribute("konto", new AppUser());
        return "rejestracja";
    }

    @PostMapping("/rejestracja")
    public String toRejestracja(AppUser appUser , RedirectAttributes ra) {


        userService.addUser(appUser);
        ra.addFlashAttribute("message", " Użytwkownik został zarejestrowany");

        return "redirect:/logowanie";
    }

    @PostMapping("/save")
    public String toSave(AppAuction appAuction){

        appAucionRepo.save(appAuction);

        return "redirect:/main";
    }

    @GetMapping("/auction-delete/{id}")
    public String deleteAuction(@PathVariable Long id){

        appAucionRepo.deleteById(id);

        return "redirect:/user";
    }

    @GetMapping("/user-delete/{id}")
    public String getDeleteUser(@PathVariable Long id){

        appUserRepo.deleteById(id);

        return "redirect:/user";
    }

    @GetMapping("/updatePrice")
    public String upDatePrice(AppAuction appAuction) {

        auctionService.changePrice(appAuction,appAuction.getPrice());

        return "index";
    }

}