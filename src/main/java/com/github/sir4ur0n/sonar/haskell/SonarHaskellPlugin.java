package com.github.sir4ur0n.sonar.haskell;

import com.github.sir4ur0n.sonar.haskell.allsides.HaskellLanguage;
import com.github.sir4ur0n.sonar.haskell.allsides.HaskellLintRulesDefinition;
import com.github.sir4ur0n.sonar.haskell.scannerside.HaskellLintIssuesLoaderSensor;
import com.github.sir4ur0n.sonar.haskell.serverside.HaskellQualityProfile;
import io.vavr.collection.List;
import org.sonar.api.Plugin;

/**
 * Plugin entry point. All code plugs happen via extensions registered here.
 */
public class SonarHaskellPlugin implements Plugin {

  @Override
  public void define(Context context) {
    List.<Object>of(HaskellLanguage.class,
        HaskellQualityProfile.class,
        HaskellLintRulesDefinition.class,
        HaskellLintIssuesLoaderSensor.class)
        .appendAll(HaskellProperties.getProperties())
        .forEach(context::addExtension);
  }
}
