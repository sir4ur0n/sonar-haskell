package com.github.sir4ur0n.sonar.haskell.scannerside;

import static io.vavr.API.Option;

import com.github.sir4ur0n.sonar.haskell.HLintError;
import com.github.sir4ur0n.sonar.haskell.HaskellProperties;
import com.github.sir4ur0n.sonar.haskell.allsides.HaskellLanguage;
import com.github.sir4ur0n.sonar.haskell.allsides.HaskellLintRulesDefinition;
import io.vavr.control.Option;
import java.io.File;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.config.Settings;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * The goal of this Sensor is to load the results of an analysis performed by an external tool named: hLint
 * Results are provided as an JSON file and are corresponding to the rules defined in 'haskelllint-rules.xml'.
 * To be very abstract, these rules are applied on source files made with Haskell.
 */
@AllArgsConstructor
public class HaskellLintIssuesLoaderSensor implements Sensor {

  private static final Logger LOGGER = Loggers.get(HaskellLintIssuesLoaderSensor.class);
  protected Settings settings;
  protected FileSystem fileSystem;

  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor.name("HaskellLint Issues Loader Sensor");
    descriptor.onlyOnLanguage(HaskellLanguage.KEY);
  }

  @Override
  public void execute(SensorContext context) {
    Option(settings.getString(HaskellProperties.REPORT_PATH_KEY))
        .filter(s -> !StringUtils.isEmpty(s))
        .forEach(reportPath -> parseAndSaveResults(context, new File(reportPath)));
  }

  private String getRepositoryKeyForLanguage(String languageKey) {
    return languageKey.toLowerCase() + "-" + HaskellLintRulesDefinition.KEY;
  }

  private void parseAndSaveResults(SensorContext context, File file) {
    LOGGER.info("Parsing 'HaskellLint' Analysis Results");
    new HaskellLintAnalysisResultsParser().parse(file)
        .forEach(error -> getResourceAndSaveIssue(context, error));
  }

  private void getResourceAndSaveIssue(SensorContext context, HLintError error) {
    LOGGER.debug(error.toString());
    FilePredicate existsAndIsMainType = fileSystem.predicates().and(
        fileSystem.predicates().hasRelativePath(error.getFilePath()),
        fileSystem.predicates().hasType(Type.MAIN));
    Option<InputFile> inputFile = Option(fileSystem.inputFile(existsAndIsMainType));
    LOGGER.debug("inputFile null ? " + inputFile.isEmpty());

    inputFile.onEmpty(() -> LOGGER.error("Unable to find an inputFile in " + error.getFilePath()))
        .forEach(file -> saveIssue(context, file, error.getLine(), error.getHint(), error.getDescription()));
  }

  private void saveIssue(SensorContext context, InputFile inputFile, int line, String externalRuleKey, String message) {
    RuleKey ruleKey = RuleKey.of(getRepositoryKeyForLanguage(inputFile.language()), externalRuleKey);

    NewIssue newIssue = context.newIssue().forRule(ruleKey);

    NewIssueLocation primaryLocation = newIssue.newLocation().on(inputFile).message(message);
    if (line > 0) {
      primaryLocation.at(inputFile.selectLine(line));
    }
    newIssue.at(primaryLocation);

    newIssue.save();
  }

  @Override
  public String toString() {
    return "HaskellLintIssuesLoaderSensor";
  }

}
