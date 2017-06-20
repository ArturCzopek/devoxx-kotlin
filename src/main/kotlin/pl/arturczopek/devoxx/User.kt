package pl.arturczopek.devoxx


import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.boot.CommandLineRunner
import org.springframework.data.repository.CrudRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.security.Principal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/**
 * @Author Artur Czopek
 * @Link http://simplecoding.pl/devoxx-kotlin
 */

@Entity
@Table(name = "Users")
data class User(@Id @Column(name = "user_id") var id: Long = 0,
                @Column(name = "username") var name: String = "Name",
                @JsonIgnore @Column(name = "password") var password: String = "Password",
                @JsonIgnore @Column(name = "enabled") var enabled: Boolean = true,
                @JsonIgnore @Column(name = "authority_id") var authorityId: Long = 0,
                @Column(name = "received") var received: Int = 0,
                @Column(name = "to_give") var toGive: Int = 10
)


interface UserRepository : CrudRepository<User, Long> {

    override fun findAll(): List<User>

    fun findOneByName(name: String): User
}


@Service
class UserInsertRunner(
        val userRepository: UserRepository,
        val authRepository: AuthRepository,
        val bCryptPasswordEncoder: BCryptPasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String?) {

        (1..20).forEach {
            val id = it.toLong()
            val name = "user$id"

            val auth = Authority(id, name, "user")
            authRepository.save(auth)

            val user = User(name = name, id = id, authorityId = id, password = bCryptPasswordEncoder.encode(name))
            userRepository.save(user)
        }
    }
}