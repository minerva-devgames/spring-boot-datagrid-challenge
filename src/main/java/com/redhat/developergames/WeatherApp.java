package com.redhat.developergames;

import javax.servlet.http.HttpSession;

import com.redhat.developergames.model.Weather;
import com.redhat.developergames.repository.WeatherRepository;

import org.infinispan.spring.remote.session.configuration.EnableInfinispanRemoteHttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
@EnableInfinispanRemoteHttpSession
public class WeatherApp {

   @Autowired
   WeatherRepository weatherRepository;

   @GetMapping("/")
   public String index() {
      return "Greetings from Spring Boot with Data Grid!";
   }

   @GetMapping("/weather/{location}")
   public Object getByLocation(@PathVariable String location, HttpSession session) {
      Weather weather = weatherRepository.getByLocation(location);
      if (weather == null) {
         return String.format("Weather for location %s not found", location);
      }
      session.setAttribute("latestLocation", location);
      return weather;
   }

   @GetMapping("/latest")
   public String latestLocation(HttpSession session) {
      String latestLocation = (String)session.getAttribute("latestLocation");
      if (StringUtils.hasLength(latestLocation)) {
          return "Your last requested location is " + latestLocation;
      } else {
          return "You have no previous requested locations";
      }
   }

   public static void main(String... args) {
      new SpringApplicationBuilder().sources(WeatherApp.class).run(args);
   }
}
