//package com.avada.myHouse24.controller;
//
//import com.avada.myHouse24.entity.User;
//import com.avada.myHouse24.repo.UserRepository;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//@AllArgsConstructor
//@Slf4j
//public class OAuth2LoginController {
//    private final OAuth2AuthorizedClientService authorizedClientService;
//    private final UserRepository userRepository;
//
//    @GetMapping("/")
//    public String handleOAuth2Callback(OAuth2AuthenticationToken authentication) {
//        log.warn("IN handleOAuth2Callback: start");
//        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
//                authentication.getAuthorizedClientRegistrationId(),
//                authentication.getName()
//        );
//        String oauthProvider = authentication.getAuthorizedClientRegistrationId();
//        Integer oauthId = authentication.getPrincipal().getAttribute("id");
//        if (oauthId != null && oauthProvider != null){
//            User existingUser = userRepository.findByOauthIdAndOauthProvider(oauthId, oauthProvider);
//        if (existingUser != null) {
//            log.info("IN handleOAuth2Callback: user exists. redirecting");
//            return "redirect:/admin/account-transaction/index";
//        }}else log.info("Something went wrong!" +"provider: "+ oauthProvider+"id: "+ oauthId);
//        log.info("IN handleOAuth2Callback: registering new user");
//        User user = new User();
//        user.setOauthProvider(oauthProvider);
//        user.setOauthId(oauthId);
//
//        userRepository.save(user);
//
//        return "redirect:/admin/account-transaction/index";
//    }
//}
//
