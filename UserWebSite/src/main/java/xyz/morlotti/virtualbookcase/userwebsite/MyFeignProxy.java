package xyz.morlotti.virtualbookcase.userwebsite;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.cloud.openfeign.FeignClient;

import xyz.morlotti.virtualbookcase.userwebsite.beans.Book;
import xyz.morlotti.virtualbookcase.userwebsite.beans.User;
import xyz.morlotti.virtualbookcase.userwebsite.beans.PreLoan;
import xyz.morlotti.virtualbookcase.userwebsite.beans.forms.Search;
import xyz.morlotti.virtualbookcase.userwebsite.security.TokenUtils;
import xyz.morlotti.virtualbookcase.userwebsite.beans.BookDescription;
import xyz.morlotti.virtualbookcase.userwebsite.beans.forms.SearchResult;

@FeignClient(name = "myFeignProxy", url = "localhost:9090")
public interface MyFeignProxy
{
	@GetMapping("/auth/login")
	String login(@RequestParam("login") String login, @RequestParam("password") String password);

	@GetMapping("/auth/remind-password")
	ResponseEntity<Void> remindPassword(@RequestParam("email") String email);

	/**/

	@GetMapping("/book/{id}")
	Book getBook(@PathVariable("id") int id);

	@GetMapping("/bookDescription/{id}")
	BookDescription getBookDescription(@PathVariable("id") int id);

	/**/

	@PostMapping("/book/search")
	Iterable<SearchResult> searchBook(@RequestBody Search search);

	/**/

	@GetMapping("/user")
	User getUser(@RequestHeader(TokenUtils.TOKEN_HEADER_NAME) String token);

	@PutMapping("/user")
	ResponseEntity<Void> updateUser(@RequestHeader(TokenUtils.TOKEN_HEADER_NAME) String token, @RequestBody User user);

	@PutMapping("/loan/{id}/extend")
	ResponseEntity<Void> extendLoan(@RequestHeader(TokenUtils.TOKEN_HEADER_NAME) String token, @PathVariable("id") int id);

	/**/

	@GetMapping("/preloans")
	Iterable<PreLoan> listPreLoans(@RequestHeader(TokenUtils.TOKEN_HEADER_NAME) String token);

	@PostMapping("/preloan/bookDescription/{bookDescriptionId}")
	ResponseEntity<Void> addPreloan(@RequestHeader(TokenUtils.TOKEN_HEADER_NAME) String token, @PathVariable("bookDescriptionId") int bookDescriptionId);

	@DeleteMapping("/preloan/{id}")
	ResponseEntity<Void> deletePreLoan(@RequestHeader(TokenUtils.TOKEN_HEADER_NAME) String token, @PathVariable("id") int id);
}
