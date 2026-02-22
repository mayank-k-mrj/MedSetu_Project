package com.MedSetu.med_setu.Config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BrowserLauncher {

    @EventListener(ApplicationReadyEvent.class)
    public void afterStartup() {

        String url = "http://localhost:8080/login";

        System.out.println("=======================================");
        System.out.println("🚀 MedSetu is running!");
        System.out.println("👉 Open this link in your browser:");
        System.out.println(url);
        System.out.println("=======================================");
    }
}