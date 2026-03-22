package com.example.demo.Controller;

import com.example.demo.Service.ShortUrlService;
import com.example.demo.entity.ShortUrl;
import com.example.demo.model.ShortenUrlRequest;
import com.example.demo.model.ShortenUrlResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
public class AppController {

    private final ShortUrlService shortUrlService;

    public AppController(ShortUrlService shortUrlService){
        this.shortUrlService=shortUrlService;
    }

    /// Displays the form page for URL shortening
    @GetMapping("/")
    public String showForm(Model model){
        model.addAttribute("request",new ShortenUrlRequest());
        return "index";
    }

    /// Processes the form Submission and creates a short URL
    @PostMapping("/shorten")
    public String shortUrl(@Valid ShortenUrlRequest request, BindingResult bindingResult, Model model, HttpServletRequest httpRequest){
        if(bindingResult.hasErrors())
            return "index";
        ShortUrl shortUrl = shortUrlService.createShortUrl(request.getUrl());

        String baseUrl = getBaseUrl(httpRequest);
        String shortUrlString = baseUrl+"/"+shortUrl.getShortCode();

        ShortenUrlResponse urlResponse = new ShortenUrlResponse();
        urlResponse.setShortCode(shortUrl.getShortCode());
        urlResponse.setShortUrl(shortUrlString);
        urlResponse.setOriginalUrl(shortUrl.getOriginalUrl());
        urlResponse.setCreatedAt(shortUrl.getCreatedAt());
        urlResponse.setExpiresAt(shortUrl.getExpiresAt());

        model.addAttribute("response",urlResponse);
        model.addAttribute("request",new ShortenUrlRequest());
        return "index";
    }

    public String getBaseUrl(HttpServletRequest httpServletRequest){
        /// method to get the base url from the request
        String scheme = httpServletRequest.getScheme();
        String serverName = httpServletRequest.getServerName();
        int serverPort = httpServletRequest.getServerPort();
        String contextPath = httpServletRequest.getContextPath();

        StringBuilder baseUrl = new StringBuilder();
        baseUrl.append(scheme).append("://").append(serverName);

        /// only append the port if it's not the default port
        if((scheme.equals("http") && serverPort!=80) || (scheme.equals("https") && serverPort != 443)){
            baseUrl.append(":").append(serverPort);
        }

        baseUrl.append(contextPath);
        return baseUrl.toString();
    }

    @GetMapping("/{shortCode}")
    public String redirectToOriginalUrl(@PathVariable String shortCode){
        Optional<String> originalUrl = shortUrlService.getOriginalUrl(shortCode);
        if(originalUrl.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Short code not found");
        }

        String url = originalUrl.get();
        if(!url.startsWith("http://")&&(!url.startsWith("https://"))){
            url="https://"+url;
        }

        return "redirect:" + url;
    }
}
