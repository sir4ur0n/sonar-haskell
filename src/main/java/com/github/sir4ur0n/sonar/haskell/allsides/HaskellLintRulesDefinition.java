package com.github.sir4ur0n.sonar.haskell.allsides;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * Define rules from XML file
 */
public class HaskellLintRulesDefinition implements RulesDefinition {

  public static final String KEY = "haskelllint";
  private static final String REPO_KEY = HaskellLanguage.KEY + "-" + KEY;
  private static final String NAME = "HaskellLint";
  private static final String REPO_NAME = HaskellLanguage.KEY + "-" + NAME;
  private static final String PATH_TO_RULES_XML = "/haskelllint-rules.xml";
  private static final Logger LOGGER = Loggers.get(HaskellLintRulesDefinition.class);

  @Override
  public void define(Context context) {
    defineRulesForLanguage(context);
  }

  private String rulesDefinitionFilePath() {
    return PATH_TO_RULES_XML;
  }

  private void defineRulesForLanguage(Context context) {
    NewRepository repository = context.createRepository(HaskellLintRulesDefinition.REPO_KEY, HaskellLanguage.KEY)
        .setName(HaskellLintRulesDefinition.REPO_NAME);

    InputStream rulesXml = getClass().getResourceAsStream(rulesDefinitionFilePath());
    if (rulesXml != null) {
      RulesDefinitionXmlLoader rulesLoader = new RulesDefinitionXmlLoader();
      rulesLoader.load(repository, rulesXml, StandardCharsets.UTF_8.name());
    } else {
      LOGGER.warn("Unable to load " + PATH_TO_RULES_XML);
    }
    repository.done();
    LOGGER.info("The repository " + HaskellLintRulesDefinition.REPO_NAME + " is made: " + repository.rules());
  }

}
