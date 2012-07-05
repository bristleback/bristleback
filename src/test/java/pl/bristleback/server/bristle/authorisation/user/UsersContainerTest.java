package pl.bristleback.server.bristle.authorisation.user;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.users.IdentifiedUser;
import pl.bristleback.server.bristle.authorisation.conditions.SendCondition;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Pawel Machowski
 * created at 01.05.12 14:35
 */
@Ignore
public class UsersContainerTest {

  private UsersContainer usersContainer;
  private MockUser mockUserA;
  private MockUser mockUserB;

  @Before
  public void setUp() throws Exception {
    usersContainer = new UsersContainer();

    WebsocketConnector aConnector = mock(WebsocketConnector.class);
    when(aConnector.getConnectorId()).thenReturn("A");
    mockUserA = new MockUser("A");
    usersContainer.newUser(aConnector);

    WebsocketConnector bConnector = mock(WebsocketConnector.class);
    when(bConnector.getConnectorId()).thenReturn("B");
    mockUserB = new MockUser("B");
    usersContainer.newUser(bConnector);

    WebsocketConnector cConnector = mock(WebsocketConnector.class);
    when(cConnector.getConnectorId()).thenReturn("C");
    usersContainer.newUser(cConnector);
  }

  @Test
  public void shouldReturnAllConnectors() throws Exception {
    //given 3 users

    //when filtering
    List<WebsocketConnector> connectorByCondition = usersContainer.getConnectorsByCondition(new AllUsersCondition());

    //then
    assertThat(connectorByCondition.size(), is(3));
  }

  @Test
  public void shouldReturnZeroConnectors() throws Exception {
    //given 3 users

    //when filtering
    List<WebsocketConnector> connectorByCondition = usersContainer.getConnectorsByCondition(new NoneCondition());

    //then
    assertThat(connectorByCondition.size(), is(0));
  }

  @Test
  public void shouldReturnNoConnectors() throws Exception {
    //given 2 users subset
    List<IdentifiedUser> users = new ArrayList<IdentifiedUser>();
    users.add(mockUserA);
    users.add(mockUserB);

    //when filtering
    List<WebsocketConnector> connectorByCondition =
      usersContainer.getConnectorsByCondition(users, new UserNameCondition("C"));

    //then
    assertThat(connectorByCondition.size(), is(0));
  }

  @Test
  public void shouldReturnConnectorFromSubSet() throws Exception {
    //given 2 users subset
    List<IdentifiedUser> users = new ArrayList<IdentifiedUser>();
    users.add(mockUserA);
    users.add(mockUserB);

    //when filtering
    List<WebsocketConnector> connectorByCondition =
      usersContainer.getConnectorsByCondition(users, new UserNameCondition("B"));

    //then
    assertThat(connectorByCondition.size(), is(1));
    assertThat(connectorByCondition.get(0).getConnectorId(), is("B"));
  }

  @Test
  public void shouldReturnConnectorWithCondition() throws Exception {
    //given 3 users

    //when filtering
    List<WebsocketConnector> connectorByCondition = usersContainer.getConnectorsByCondition(new UserNameCondition("A"));

    //then
    assertThat(connectorByCondition.size(), is(1));
    assertThat(connectorByCondition.get(0).getConnectorId(), is("A"));
  }

}

class UserNameCondition implements SendCondition<MockUser> {

  private String userName;

  public UserNameCondition(String userName) {
    this.userName = userName;
  }

  @Override
  public boolean isApplicable(MockUser user) {
    return user.getName().equals(userName);
  }
}

class AllUsersCondition implements SendCondition<IdentifiedUser> {
  @Override
  public boolean isApplicable(IdentifiedUser user) {
    return true;
  }
}

class NoneCondition implements SendCondition<IdentifiedUser> {
  @Override
  public boolean isApplicable(IdentifiedUser user) {
    return false;
  }
}
