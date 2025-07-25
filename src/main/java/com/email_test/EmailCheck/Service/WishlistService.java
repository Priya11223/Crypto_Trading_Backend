package com.email_test.EmailCheck.Service;

import com.email_test.EmailCheck.Modal.Coin;
import com.email_test.EmailCheck.Modal.User;
import com.email_test.EmailCheck.Modal.Wishlist;
import com.email_test.EmailCheck.Repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {
    @Autowired
    WishlistRepository wishlistRepository;

    public Wishlist findUserWishlist(Long userId) throws Exception {
        Wishlist watchlist = wishlistRepository.findByUserId(userId);
        if(watchlist==null)
        {
            throw new Exception("watchlist not found");
        }
        return watchlist;
    }

    public Wishlist createWishlist(User user) {
        Wishlist watchlist = new Wishlist();
        watchlist.setUser(user);

        return wishlistRepository.save(watchlist);
    }

    public void addItemToWatchList(Coin coin, User user) throws Exception {
        Wishlist watchlist = findUserWishlist(user.getId());
        if(watchlist.getCoins().contains(coin))
            watchlist.getCoins().remove(coin);
        else
            watchlist.getCoins().add(coin);
        wishlistRepository.save(watchlist);
    }
}
