package xyz.morlotti.virtualbookcase.webapi.models;

import java.util.Set;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "BOOKDESCRIPTION")
@Table(name = "BOOKDESCRIPTION", catalog = "virtualbookcase")
public class BookDescription implements java.io.Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @NotEmpty
    @Length(min = 10, max = 13, message = "L'ISBN doit comporter entre 10 et 13 caractères")
    @Column(name = "isbn", nullable = false, length = 13)
    private String isbn;

    @NotEmpty
    @Column(name = "title", nullable = false, length = 256)
    private String title;

    @NotEmpty
    @Column(name = "authorFirstname", nullable = false, length = 128)
    private String authorFirstname;

    @NotEmpty
    @Column(name = "authorLastname", nullable = false, length = 128)
    private String authorLastname;

    @NotEmpty
    @Column(name = "editor", nullable = false, length = 128)
    private String editor;

    @Column(name = "editionNumber", nullable = false)
    private Integer editionNumber;

    @Length(min = 4, max = 4, message = "L'année d'édition doit compter 4 chiffres")
    @Column(name = "editionYear", nullable = false, columnDefinition = "YEAR")
    private Integer editionYear;

    @NotEmpty
    @Column(name = "genre", nullable = false, length = 64)
    private String genre;

    @NotEmpty
    @Column(name = "format", nullable = false, length = 64)
    private String format;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @org.hibernate.annotations.CreationTimestamp
    @Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date created;

    ////////

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "bookDescription")
    private Set<Book> books;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "bookDescription")
    private Set<PreLoan> preLoans;
}
