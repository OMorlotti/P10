package xyz.morlotti.virtualbookcase.webapi.models;

import java.util.Set;
import java.util.Date;
import java.time.LocalDate;
import java.util.stream.Stream;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import xyz.morlotti.virtualbookcase.webapi.exceptions.APIInvalidValueException;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "USER")
@Table(name = "USER")
public class User implements java.io.Serializable
{
    public enum Sex
    {
        F("F", 0), H("H", 1);

        private final String value;
        private final int code;

        private Sex(String value, int code) /* Une valeur d'enum est comme une classe, elle peut avoir un constructeur et des méthodes : https://stackoverflow.com/questions/13291076/java-enum-why-use-tostring-instead-of-name */
        {
            this.value = value;
            this.code = code;
        }

        public String toString() /* Pour convertir une valeur d'enum en String */
        {
            return value;
        }

        public int toCode() /* Pour convertir une valeur d'enum en entier */
        {
            return code;
        }

        public static Sex parseString(String value) /* Pour convertir une chaîne en une valeur d'enum */
        {
            return Stream.of(values()).filter(x -> x.value.equalsIgnoreCase(value)).findFirst().orElseThrow(() -> new APIInvalidValueException("Invalid sex name " + value));
        }

        public static Sex parseCode(int code) /* Pour convertir un entier en une valeur d'enum */
        {
            return Stream.of(values()).filter(x -> x.code == code).findFirst().orElseThrow(() -> new APIInvalidValueException("Invalid sex code " + code));
        }
    }

    ////////

    public enum Role
    {
        ADMIN("ADMIN", 0),
        EMPLOYEE("EMPLOYEE", 1),
        USER("USER", 2);

        private final String value;
        private final int code;

        private Role(String value, int code) /* Une valeur d'enum est comme une classe, elle peut avoir un constructeur et des méthodes : https://stackoverflow.com/questions/13291076/java-enum-why-use-tostring-instead-of-name */
        {
            this.value = value;
            this.code = code;
        }

        public String toString() /* Pour convertir une valeur d'enum en String */
        {
            return value;
        }

        public int toCode() /* Pour convertir une valeur d'enum en entier */
        {
            return code;
        }

        public static Role parseString(String value) /* Pour convertir une chaîne en une valeur d'enum */
        {
            return Stream.of(values()).filter(x -> x.value.equalsIgnoreCase(value)).findFirst().orElseThrow(() -> new APIInvalidValueException("Invalid role name " + value));
        }

        public static Role parseCode(int code) /* Pour convertir un entier en une valeur d'enum */
        {
            return Stream.of(values()).filter(x -> x.code == code).findFirst().orElseThrow(() -> new APIInvalidValueException("Invalid role code " + code));
        }
    }

    ////////

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @NotEmpty
    @Column(name = "login", unique = true, nullable = false, length = 64)
    private String login;

    // Peut être vide, voir plus bas
    // Pour ne pas exposer le password lors d'un GET
    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @NotEmpty
    @Column(name = "firstname", nullable = false, length = 256)
    private String firstname;

    @NotEmpty
    @Column(name = "lastname", nullable = false, length = 256)
    private String lastname;

    @NotEmpty
    @Column(name = "streetNb", nullable = false, length = 32)
    private String streetNb;

    @NotEmpty
    @Column(name = "streetName", nullable = false, length = 256)
    private String streetName;

    @Column(name = "postalCode", nullable = false)
    private int postalCode;

    @NotEmpty
    @Column(name = "city", nullable = false, length = 256)
    private String city;

    @NotEmpty
    @Column(name = "country", nullable = false, length = 128)
    private String country;

    @NotEmpty
    @Column(name = "email", nullable = false, length = 256)
    private String email;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @Column(name = "sex", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer sex;

    public Sex getSex()
    {
        return Sex.parseCode(this.sex);
    }

    public void setSex(Sex sex)
    {
        this.sex = sex.toCode();
    }

    @Column(name = "role", nullable = false, columnDefinition = "INT DEFAULT 2")
    private Integer role;

    public Role getRole()
    {
        return Role.parseCode(this.role);
    }

    public void setRole(Role role)
    {
        this.role = role.toCode();
    }

    @Column(name = "membership", nullable = false)
    private LocalDate membership;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @org.hibernate.annotations.CreationTimestamp
    @Column(name = "created", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date created;

    ////////

    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private Set<Loan> loans;

    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private Set<PreLoan> preLoans;

    ////////

    public String getPassword()
    {
        if(this.password != null)
        {
            if(this.password.startsWith("$2a$")) // Check if the password is crypted
            {
                return this.password;
            }
            else
            {
                return new BCryptPasswordEncoder().encode(this.password);
            }
        }
        else
        {
            return null;
        }
    }

    public void setPassword(String password)
    {
        if(password != null)
        {
            if(password.startsWith("$2a$")) // Check if the password is crypted
            {
                if(!new BCryptPasswordEncoder().matches("", password))
                {
                    this.password = password;
                }
            }
            else
            {
                if(!password.isEmpty())
                {
                    this.password = new BCryptPasswordEncoder().encode(password);
                }
            }
        }
        else
        {
            return;
        }
    }
}
