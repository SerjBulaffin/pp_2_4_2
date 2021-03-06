package web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import web.config.handler.LoginSuccessHandler;
import web.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsServiceImpl = userDetailsService;
    }

//    @Bean
//    BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    /*
- SecurityConfig - настройка секьюрности по определенным URL, а также настройка UserDetails и GrantedAuthority.
 */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.inMemoryAuthentication().withUser("ADMIN").password("ADMIN").roles("ADMIN");

        //устанавливаем список ролей для чтения из базы данных, раскодируем пароль passwordEncoder(passwordEncoder()),
        //в качестве параметра передаем наш бин внизу passwordEncoder()
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder()); //.passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                // указываем страницу с формой логина
                .loginPage("/login")
                //указываем логику обработки при логине
                .successHandler(new LoginSuccessHandler())
                // указываем action с формы логина
                .loginProcessingUrl("/login")
                // Указываем параметры логина и пароля с формы логина
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                // даем доступ к форме логина всем
                .permitAll();

        http.logout()
                // разрешаем делать логаут всем
                .permitAll()
                // указываем URL логаута
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                // указываем URL при удачном логауте
                .logoutSuccessUrl("/login?logout")
                //выклчаем кроссдоменную секьюрность (на этапе обучения неважна)
                .and().csrf().disable();

        http
                // делаем страницу регистрации недоступной для авторизированных пользователей
                .authorizeRequests()
                //видимость стартовой страницы для всех
                //.antMatchers("/").permitAll()
                //страницы аутентификаци доступна всем
                .antMatchers("/login").anonymous()
                // защищенные URL
//                .antMatchers("/user").access("hasAnyRoles('USER', 'ADMIN')") //Кидает ошибку
//                .antMatchers("/hello", "/admin").access("hasAnyRole('ADMIN')")

                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();
    }

    //Кодировка - раскодировка паролей юзеров
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
/*
Оба эти интерфейса имеют множество реализаций: просмотрите класс SecurityConfig, в методе configure() с помощью настроек
inMemoryAuthentication() мы собираем единственный на всю программу экземпляр UserDetails с именем и паролем админ-админ,
а его роль “ADMIN” так же будет преобразована в экземпляр GrantedAuthority.
Это простейший способ создания секьюрности. Так же мы можем использовать jdbc-аутентификацию путем написания запроса,
возвращающего пользователя и роль.
Как вы понимаете, такие способы максимально просты, но лишены достаточной гибкости, потому наиболее часто используемый
вариант настройки выглядит как имплементация UserDetails и GrantedAuthority в классах-сущностях с переопределением существующих методов.
 */