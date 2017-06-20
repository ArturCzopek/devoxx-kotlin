package pl.arturczopek.devoxx


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.sql.DataSource

/**
 * @Author Artur Czopek
 * @Link http://simplecoding.pl/devoxx-kotlin
 */

@Configuration
class MvcConfig : WebMvcConfigurer {

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.run {
            addViewController("/home").setViewName("home")
            addViewController("/").setViewName("home")
            addViewController("/login").setViewName("login")
            addViewController("/db-console").setViewName("db-console")
        }
    }
}

@Configuration
@EnableWebSecurity
open class SecurityConfiguration(
        val bCryptPasswordEncoder: BCryptPasswordEncoder,
        val dataSource: DataSource
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests()
                .antMatchers("/login", "/db-console").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .logout().permitAll()
    }

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder)
    }
}