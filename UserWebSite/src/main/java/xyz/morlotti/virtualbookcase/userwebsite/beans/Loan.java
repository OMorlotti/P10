package xyz.morlotti.virtualbookcase.userwebsite.beans;

import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Loan implements java.io.Serializable, Comparable
{
     private Integer id;

     private Book book;

     private LocalDate loanStartDate;

     private LocalDate loanEndDate;

     private Boolean extensionAsked;

     private String comment;

     private String state;

     private LocalDate returnDate;

     private long remainingDays;

     @Override
     public int compareTo(Object o)
     {
          return this.loanStartDate.compareTo(((Loan) o).loanStartDate);
     }
}
