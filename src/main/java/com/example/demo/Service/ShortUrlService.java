package com.example.demo.Service;

import com.example.demo.Repository.ShortUrlRepository;
import com.example.demo.entity.ShortUrl;
import com.example.demo.util.ShortUrlGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ShortUrlService {

    private final ShortUrlRepository urlRepository;
    private final ShortUrlGenerator urlGenerator;
    private static final int MAX_COLLISION_RETRIES = 10;

    public ShortUrlService(ShortUrlRepository urlRepository, ShortUrlGenerator urlGenerator){
        this.urlRepository=urlRepository;
        this.urlGenerator=urlGenerator;
    }

    public ShortUrl createShortUrl(String originalUrl){
        Optional<ShortUrl> existingUrl = urlRepository.findByOriginalUrl(originalUrl);
        if(existingUrl.isPresent())
            return existingUrl.get();

        /// generate unique short code
        String shortCode = generateUniqueCode();

        /// create and save the new shortUrl
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl(originalUrl);
        shortUrl.setShortCode(shortCode);

        return urlRepository.save(shortUrl);
    }

    private String generateUniqueCode(){
        int attempt = 0;
        String shortCode;
        do{
            shortCode = urlGenerator.generate();
            attempt++;
            if(attempt > MAX_COLLISION_RETRIES){
                throw new RuntimeException("Failed to generate unique short code");
            }
        }
        while(urlRepository.findByShortCode(shortCode).isPresent());
        return shortCode;
    }

    @Transactional(readOnly = true)
    public Optional<String> getOriginalUrl(String shortCode){
        return urlRepository.findByShortCode(shortCode).map(ShortUrl::getOriginalUrl);
    }
}
