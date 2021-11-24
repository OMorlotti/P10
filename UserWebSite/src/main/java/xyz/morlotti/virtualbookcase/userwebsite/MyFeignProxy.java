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

@FeignClient(name = "myFeignProxy", url = "192.168.0.78:9090")
public interface MyFeignProxy
{
	@GetMapping("/auth/login")
	public String login(@RequestParam("login") String login, @RequestParam("password") String password);

	@GetMapping("/auth/remind-password")
	public ResponseEntity<Void> remindPassword(@RequestParam("email") String email);

	/**/

	@GetMapping("/book/{id}")
	public Book getBook(@PathVariable("id") int id);

	@GetMapping("/bookDescription/{id}")
	public BookDescription getBookDescription(@PathVariable("id") int id);

	/**/

	@PostMapping("/book/search")
	public Iterable<SearchResult> searchBook(@RequestBody Search search);

	/**/

	@GetMapping("/user")
	public User getUser(@RequestHeader(TokenUtils.TOKEN_HEADER_NAME) String token);

	@PutMapping("/user")
	public ResponseEntity<Void> updateUser(@RequestHeader(TokenUtils.TOKEN_HEADER_NAME) String token, @RequestBody User user);

	@PutMapping("/loan/{id}/extend")
	public ResponseEntity<Void> extendLoan(@RequestHeader(TokenUtils.TOKEN_HEADER_NAME) String token, @PathVariable("id") int id);

	/**/

	@GetMapping("/preloans")
	public Iterable<PreLoan> listPreLoans(@RequestHeader(TokenUtils.TOKEN_HEADER_NAME) String token);
}
