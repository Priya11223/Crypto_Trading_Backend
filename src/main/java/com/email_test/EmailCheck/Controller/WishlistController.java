package com.email_test.EmailCheck.Controller;

import com.email_test.EmailCheck.Modal.Coin;
import com.email_test.EmailCheck.Modal.User;
import com.email_test.EmailCheck.Service.CoinService;
import com.email_test.EmailCheck.Service.UserService;
import com.email_test.EmailCheck.Service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/add/{coinId}")
    public ResponseEntity<?> addItemToWatchlist(@PathVariable String coinId) throws Exception {
        User user = userService.findUserByJwt();
        Coin coin = coinService.getCoinDetails(coinId);
        wishlistService.addItemToWatchList(coin, user);
        return ResponseEntity.ok("Coin Added successFully");
    }
}
