package pl.bristleback.server.bristle.engine.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.bristleback.server.bristle.api.users.UserFactory;
import pl.bristleback.server.mock.MockUserFactory;
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
    assertThat(defaultUserFactory.createNewUser(), notNullValue());
  }

  @Test
  public void shouldCreateDefaultUserWhenUserFactoryNotFound() throws Exception {
    UserFactory defaultUserFactory = getUserFactory();
    assertThat(defaultUserFactory.createNewUser(), instanceOf(DefaultUser.class));
  }*/

  @Test
  public void shouldCreateUserWhenUserFactoryFound() throws Exception {
    UserFactory userFactory = getUserFactory();
    assertThat(userFactory.createNewUser().getId(), is(MockUserFactory.MockUser.MOCK_USER_ID));
  }


  private UserFactory getUserFactory() {
    return mockBeansFactory.getFrameworkBean("userFactory", UserFactory.class);
  }

}
