package com.github.sir4ur0n.sonar.haskell.serverside;

import static java.util.Collections.emptyList;

import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import org.sonar.api.profiles.AnnotationProfileParser;
import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.profiles.XMLProfileParser;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * Define Sonar way profile using XML file in which rules priorities are known
 */
@AllArgsConstructor
public class HaskellQualityProfile extends ProfileDefinition {

  private static final String PROFILE_NAME = "Sonar way";
  private static final Logger LOGGER = Loggers.get(HaskellQualityProfile.class);
  private final AnnotationProfileParser annotationProfileParser;
  private final XMLProfileParser xmlProfileParser;

  @Override
  public RulesProfile createProfile(ValidationMessages validation) {
    LOGGER.info("Haskell profile loading...");

    RulesProfile profile = RulesProfile.create(PROFILE_NAME, "haskell");
    profile.setDefaultProfile(Boolean.TRUE);

    RulesProfile checks = annotationProfileParser.parse("haskell-haskelllint",
        "haskell-HaskellLint", "haskell", emptyList(), validation);

    RulesProfile dialyzer = xmlProfileParser.parseResource(getClass().getClassLoader(),
        "default-profile.xml", validation);

    List<ActiveRule> rules = List.ofAll(checks.getActiveRules())
        .appendAll(dialyzer.getActiveRules());

    profile.setActiveRules(rules.toJavaList());

    LOGGER.info("Haskell profile loaded with rules: " + profile.getActiveRules());
    return profile;
  }

}
