package pl.bristleback.server.bristle.security.authorisation.conditions;

import org.junit.Test;
import pl.bristleback.server.bristle.api.action.SendCondition;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.engine.user.DefaultUserContextFactory;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class LogicalConditionsTest {

  private SendCondition<UserContext> createAcceptingCondition() {
    return AllUsersCondition.getInstance();
  }

  private SendCondition<UserContext> createRefusingCondition() {
    return new SendCondition<UserContext>() {
      @Override
      public boolean isApplicable(UserContext userContext) {
        return false;
      }
    };
  }

  @Test
  public void testAndConditionTwoConditionsAccepting() throws Exception {
    //given
    SendCondition<UserContext> acceptingCondition1 = createAcceptingCondition();
    SendCondition<UserContext> acceptingCondition2 = createAcceptingCondition();

    SendCondition<UserContext> andCondition = LogicalConditions.and(acceptingCondition1, acceptingCondition2);

    UserContext user = new DefaultUserContextFactory().createNewUserContext();

    //when
    boolean isApplicable = andCondition.isApplicable(user);

    //then
    assertTrue(isApplicable);
  }

  @Test
  public void testAndConditionOneConditionAccepting() throws Exception {
    //given
    SendCondition<UserContext> acceptingCondition = createAcceptingCondition();
    SendCondition<UserContext> refusingCondition = createRefusingCondition();

    SendCondition<UserContext> andCondition = LogicalConditions.and(acceptingCondition, refusingCondition);

    UserContext user = new DefaultUserContextFactory().createNewUserContext();

    //when
    boolean isApplicable = andCondition.isApplicable(user);

    //then
    assertFalse(isApplicable);
  }

  @Test
  public void testAndConditionThreeConditionsAccepting() throws Exception {
    //given
    SendCondition<UserContext> acceptingCondition1 = createAcceptingCondition();
    SendCondition<UserContext> acceptingCondition2 = createAcceptingCondition();
    SendCondition<UserContext> acceptingCondition3 = createAcceptingCondition();

    SendCondition<UserContext> andCondition = LogicalConditions.and(acceptingCondition1, acceptingCondition2, acceptingCondition3);

    UserContext user = new DefaultUserContextFactory().createNewUserContext();

    //when
    boolean isApplicable = andCondition.isApplicable(user);

    //then
    assertTrue(isApplicable);
  }

  @Test
  public void testOrConditionOneConditionAccepting() throws Exception {
    //given
    SendCondition<UserContext> acceptingCondition = createAcceptingCondition();
    SendCondition<UserContext> refusingCondition = createRefusingCondition();

    SendCondition<UserContext> orCondition = LogicalConditions.or(acceptingCondition, refusingCondition);

    UserContext user = (UserContext) new DefaultUserContextFactory().createNewUserContext();

    //when
    boolean isApplicable = orCondition.isApplicable(user);

    //then
    assertTrue(isApplicable);
  }

  @Test
  public void testOrConditionNoneAccepting() throws Exception {
    //given
    SendCondition<UserContext> refusingCondition1 = createRefusingCondition();
    SendCondition<UserContext> refusingCondition2 = createRefusingCondition();

    SendCondition<UserContext> orCondition = LogicalConditions.or(refusingCondition1, refusingCondition2);

    UserContext user = (UserContext) new DefaultUserContextFactory().createNewUserContext();

    //when
    boolean isApplicable = orCondition.isApplicable(user);

    //then
    assertFalse(isApplicable);
  }

  @Test
  public void testOrConditionTwoConditionAccepting() throws Exception {
    //given
    SendCondition<UserContext> acceptingCondition1 = createAcceptingCondition();
    SendCondition<UserContext> acceptingCondition2 = createAcceptingCondition();
    SendCondition<UserContext> refusingCondition = createRefusingCondition();

    SendCondition<UserContext> orCondition = LogicalConditions.or(acceptingCondition1, refusingCondition, acceptingCondition2);

    UserContext user = (UserContext) new DefaultUserContextFactory().createNewUserContext();

    //when
    boolean isApplicable = orCondition.isApplicable(user);

    //then
    assertTrue(isApplicable);
  }

  @Test
  public void testNotConditionFromPositiveToNegative() throws Exception {
    //given
    SendCondition<UserContext> acceptingCondition = createAcceptingCondition();

    SendCondition<UserContext> orCondition = LogicalConditions.not(acceptingCondition);

    UserContext user = (UserContext) new DefaultUserContextFactory().createNewUserContext();

    //when
    boolean isApplicable = orCondition.isApplicable(user);

    //then
    assertFalse(isApplicable);
  }

  @Test
  public void testNotConditionFromNegativeToPositive() throws Exception {
    //given
    SendCondition<UserContext> refusingCondition = createRefusingCondition();

    SendCondition<UserContext> orCondition = LogicalConditions.not(refusingCondition);

    UserContext user = (UserContext) new DefaultUserContextFactory().createNewUserContext();

    //when
    boolean isApplicable = orCondition.isApplicable(user);

    //then
    assertTrue(isApplicable);
  }
}
