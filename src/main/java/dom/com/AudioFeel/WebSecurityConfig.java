package dom.com.AudioFeel;

import dom.com.AudioFeel.Repo.AppUserRepo;
import dom.com.AudioFeel.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImp userDetailsServiceImp;
    private AppUserRepo appUserRepo;

    @Autowired
    AuthenticationSuccessHandler successHandler;

    @Autowired
    public WebSecurityConfig(UserDetailsServiceImp userDetailsServiceImp,AppUserRepo appUserRepo) {
        this.userDetailsServiceImp = userDetailsServiceImp;
        this.appUserRepo = appUserRepo;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImp);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/user").hasAuthority("ROLE_USER")
                .antMatchers("/admin").hasAuthority("ROLE_ADMIN")
                .antMatchers("/audio").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .and()
                .formLogin()
                .loginPage("/logowanie").successHandler(successHandler).permitAll()
                .and().logout(logout -> logout.logoutUrl("/logout"));

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void get(){
        AppUser appUser = new AppUser("Daniel",passwordEncoder().encode("test"),"ROLE_USER");
        AppUser appUser1 = new AppUser("Mariusz",passwordEncoder().encode("test"),"ROLE_ADMIN");

        appUserRepo.save(appUser);
        appUserRepo.save(appUser1);

    }

}











