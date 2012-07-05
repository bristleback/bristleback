package pl.bristleback.server.bristle.authorisation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Pawel Machowski
 * created at 11.02.12 15:44
 */
@Configuration
public class AuthorisationSourceProvider {

  @Bean
  public ActionNameAuthorisationPolicy connectorAuthorisationPolicy() {
    return new ActionNameAuthorisationPolicy();
  }
}
