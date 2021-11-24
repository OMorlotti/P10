package xyz.morlotti.virtualbookcase.userwebsite.beans;

import java.util.List;
import java.time.LocalDate;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User implements java.io.Serializable
{
     private Integer id;

     private String login;

     private String password;

     private String firstname;

     private String lastname;

     private String streetNb;

     private String streetName;

     private int postalCode;

     private String city;

     private String country;

     private String email;

     private LocalDate birthdate;

     private String sex;

     private String role;

     private LocalDate membership;

     private String created;

     ////////

     private List<Loan> loans;

     private List<PreLoan> preLoans;

}
