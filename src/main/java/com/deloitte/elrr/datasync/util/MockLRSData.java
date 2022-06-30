package com.deloitte.elrr.datasync.util;

import gov.adlnet.xapi.model.Account;
import gov.adlnet.xapi.model.Activity;
import gov.adlnet.xapi.model.ActivityDefinition;
import gov.adlnet.xapi.model.Actor;
import gov.adlnet.xapi.model.Agent;
import gov.adlnet.xapi.model.IStatementObject;
import gov.adlnet.xapi.model.Result;
import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.StatementResult;
import gov.adlnet.xapi.model.Verb;
import java.util.ArrayList;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
final class MockLRSData {

    /**
     *
     */
   private MockLRSData() {

   }

  /**
   *
   * @return StatementResult
   */
  public static StatementResult getLearnerStatements() {
    return getStatements();
  }

  private static StatementResult getStatements() {
    log.info("inside test");
    StatementResult statements = new StatementResult();
    ArrayList<Statement> list = new ArrayList<>();
    list.add(getStatement1());
    list.add(getStatement2());
    statements.setStatements(list);
    return statements;
  }

  private static Statement getStatement1() {
    Statement statement = createStatement();
    statement.setActor(
      getAgentActor("Cristopher Cunningham", "mailto:c.cooper3@yahoo.com")
    );
    statement.setId("4859303f-8237-46d2-8bf3-186069514eb7");
    statement.setObject(
      getObject(
        "http://adlnet.gov/expapi/activities/example/CLC%20104",
        "Analyzing Profit or Fee"
      )
    );
    statement.setVerb(getVerb("resumed"));
    return statement;
  }

  private static Statement getStatement2() {
    Statement statement = createStatement();
    statement.setActor(
      getAgentActor("Bill Phillips", "mailto:b.phillp3@hotmail.com")
    );
    statement.setId("700dc3e1-2eb1-416f-9cad-b20c9bf33a93");
    statement.setObject(
      getObject(
        "http://adlnet.gov/expapi/activities/example/RQM%20310",
        "Advanced Concepts and Skills for Requirements Management"
      )
    );
    statement.setVerb(getVerb("completed"));
    return statement;
  }

  private static Statement createStatement() {
    Statement statement = new Statement();
    statement.setAttachments(null);
    statement.setAuthority(getAuthority());
    statement.setContext(null);
    Result result = new Result();
    result.setSuccess(true);
    statement.setResult(result);
    statement.setStored("2021-05-04T23:30:40.014Z");
    statement.setTimestamp("2021-05-04T23:30:40.014Z");
    statement.setVersion(null);
    return statement;
  }
  /**
   *
   * @param string
   * @return Verb
   */
  private static Verb getVerb(final String string) {
    Verb verb = new Verb();
    verb.setId("http://adlnet.gov/expapi/verbs/" + string);
    HashMap<String, String> hashMap = new HashMap<>();
    hashMap.put("en-US", string);
    verb.setDisplay(hashMap);
    return verb;
  }
  /**
   *
   * @param id
   * @param courseName
   * @return IStatementObject
   */
  private static IStatementObject getObject(final String id,
          final String courseName) {
    Activity activity = new Activity();
    activity.setId(id);
    ActivityDefinition definition = new ActivityDefinition();
    definition.setType(null);
    definition.setMoreInfo(null);
    definition.setInteractionType(null);
    HashMap<String, String> hashMap = new HashMap<>();
    hashMap.put("en-US", courseName);
    definition.setExtensions(null);
    definition.setName(hashMap);
    definition.setDescription(null);
    definition.setChoices(null);
    definition.setScale(null);
    definition.setTarget(null);
    definition.setSteps(null);
    activity.setDefinition(definition);

    return activity;
  }

  private static Agent getAgentActor(final String name, final String email) {
    Agent actor = new Agent();
    actor.setAccount(null);
    actor.setMbox(email);
    actor.setMbox_sha1sum(null);
    actor.setName(name);
    return actor;
  }

  private static Actor getAuthority() {
    Actor actor = new Agent();
    Account account = new Account();
    account.setHomePage(
      "https://deloitte-prototype-noisy.lrs.io/keys/ELRR_Validation"
    );
    account.setName("ELRR_Validation");
    actor.setAccount(account);
    actor.setMbox(null);
    actor.setMbox_sha1sum(null);
    actor.setName(null);
    return actor;
  }
}
