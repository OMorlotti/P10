package xyz.morlotti.virtualbookcase.webapi.daos.custom;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdvancedSearch
{
	private String title;

	private String author;

	private String editionNumber;

	private String editionYear;

	private String editor;

	private String isbn;
}
