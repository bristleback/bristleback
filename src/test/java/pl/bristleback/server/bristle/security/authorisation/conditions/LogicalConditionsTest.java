package pl.bristleback.server.bristle.security.authorisation.conditions;

import org.junit.Test;
import pl.bristleback.server.bristle.api.action.SendCondition;
import pl.bristleback.server.bristle.engine.base.users.DefaultUser;
import pl.bristleback.server.bristle.engine.base.users.DefaultUserFactory;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class LogicalConditionsTest {

  private SendCondition<DefaultUser> createAcceptingCondition() {
    return AllUsersCondition.getInstance();
  }

  private SendCondition<DefaultUser> createRefusingCondition() {
    return new SendCondition<DefaultUser>() {
      @Override
      public boolean isApplicable(DefaultUser user) {
        return false;
      }
    };
  }

  @Test
  public void testAndConditionTwoConditionsAccepting() throws Exception {
    //given
    SendCondition<DefaultUser> acceptingCondition1 = createAcceptingCondition();
    SendCondition<DefaultUser> acceptingCondition2 = createAcceptingCondition();

    SendCondition<DefaultUser> andCondition = LogicalConditions.and(acceptingCondition1, acceptingCondition2);

    DefaultUser user = (DefaultUser) new DefaultUserFactory().createNewUser();

    //when
    boolean isApplicable = andCondition.isApplicable(user);

    //then
    assertTrue(isApplicable);
  }

  @Test
  public void testAndConditionOneConditionAccepting() throws Exception {
    //given
    SendCondition<DefaultUser> acceptingCondition = createAcceptingCondition();
    SendCondition<DefaultUser> refusingCondition = createRefusingCondition();

    SendCondition<DefaultUser> andCondition = LogicalConditions.and(acceptingCondition, refusingCondition);

    DefaultUser user = (DefaultUser) new DefaultUserFactory().createNewUser();

    //when
    boolean isApplicable = andCondition.isApplicable(user);

    //then
    assertFalse(isApplicable);
  }

  @Test
  public void testAndConditionThreeConditionsAccepting() throws Exception {
    //given
    SendCondition<DefaultUser> acceptingCondition1 = createAcceptingCondition();
    SendCondition<DefaultUser> acceptingCondition2 = createAcceptingCondition();
    SendCondition<DefaultUser> acceptingCondition3 = createAcceptingCondition();

    SendCondition<DefaultUser> andCondition = LogicalConditions.and(acceptingCondition1, acceptingCondition2, acceptingCondition3);

    DefaultUser user = (DefaultUser) new DefaultUserFactory().createNewUser();

    //when
    boolean isApplicable = andCondition.isApplicable(user);

    //then
    assertTrue(isApplicable);
  }

  @Test
  public void testOrConditionOneConditionAccepting() throws Exception {
    //given
    SendCondition<DefaultUser> acceptingCondition = createAcceptingCondition();
    SendCondition<DefaultUser> refusingCondition = createRefusingCondition();

    SendCondition<DefaultUser> orCondition = LogicalConditions.or(acceptingCondition, refusingCondition);

    DefaultUser user = (DefaultUser) new DefaultUserFactory().createNewUser();

    //when
    boolean isApplicable = orCondition.isApplicable(user);

    //then
    assertTrue(isApplicable);
  }

  @Test
  public void testOrConditionNoneAccepting() throws Exception {
    //given
    SendCondition<DefaultUser> refusingCondition1 = createRefusingCondition();
    SendCondition<DefaultUser> refusingCondition2 = createRefusingCondition();

    SendCondition<DefaultUser> orCondition = LogicalConditions.or(refusingCondition1, refusingCondition2);

    DefaultUser user = (DefaultUser) new DefaultUserFactory().createNewUser();

    //when
    boolean isApplicable = orCondition.isApplicable(user);

    //then
    assertFalse(isApplicable);
  }

  @Test
  public void testOrConditionTwoConditionAccepting() throws Exception {
    //given
    SendCondition<DefaultUser> acceptingCondition1 = createAcceptingCondition();
    SendCondition<DefaultUser> acceptingCondition2 = createAcceptingCondition();
    SendCondition<DefaultUser> refusingCondition = createRefusingCondition();

    SendCondition<DefaultUser> orCondition = LogicalConditions.or(acceptingCondition1, refusingCondition, acceptingCondition2);

    DefaultUser user = (DefaultUser) new DefaultUserFactory().createNewUser();

    //when
    boolean isApplicable = orCondition.isApplicable(user);

    //then
    assertTrue(isApplicable);
  }

  @Test
  public void testNotConditionFromPositiveToNegative() throws Exception {
    //given
    SendCondition<DefaultUser> acceptingCondition = createAcceptingCondition();

    SendCondition<DefaultUser> orCondition = LogicalConditions.not(acceptingCondition);

    DefaultUser user = (DefaultUser) new DefaultUserFactory().createNewUser();

    //when
    boolean isApplicable = orCondition.isApplicable(user);

    //then
    assertFalse(isApplicable);
  }

  @Test
  public void testNotConditionFromNegativeToPositive() throws Exception {
    //given
    SendCondition<DefaultUser> refusingCondition = createRefusingCondition();

    SendCondition<DefaultUser> orCondition = LogicalConditions.not(refusingCondition);

    DefaultUser user = (DefaultUser) new DefaultUserFactory().createNewUser();

    //when
    boolean isApplicable = orCondition.isApplicable(user);

    //then
    assertTrue(isApplicable);
  }
}
