package pl.bristleback.server.bristle.engine.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.bristleback.server.bristle.api.users.UserContextFactory;
import pl.bristleback.server.mock.MockUserContextFactory;
import pl.bristleback.server.mock.beans.SpringMockBeansFactory;

import javax.inject.Inject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Pawel Machowski
 * created at 03.06.12 15:10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-context.xml"})
public class UserInstanceProviderTest {

  @Inject
  private SpringMockBeansFactory mockBeansFactory;

//  TODO find way to run that tests
/*  @Test
  public void shouldCreateUserWhenUserFactoryNotFound() throws Exception {
    UserFactory defaultUserFactory = getUserFactory();
    assertThat(defaultUserFactory.createNewUserContext(), notNullValue());
  }

  @Test
  public void shouldCreateDefaultUserWhenUserFactoryNotFound() throws Exception {
    UserFactory defaultUserFactory = getUserFactory();
    assertThat(defaultUserFactory.createNewUserContext(), instanceOf(DefaultUser.class));
  }*/

  @Test
  public void shouldCreateUserWhenUserFactoryFound() throws Exception {
    UserContextFactory userContextFactory = getUserFactory();
    assertThat(userContextFactory.createNewUserContext().getId(), is(MockUserContextFactory.MockUserContext.MOCK_USER_ID));
  }


  private UserContextFactory getUserFactory() {
    return mockBeansFactory.getFrameworkBean("userFactory", UserContextFactory.class);
  }

}
