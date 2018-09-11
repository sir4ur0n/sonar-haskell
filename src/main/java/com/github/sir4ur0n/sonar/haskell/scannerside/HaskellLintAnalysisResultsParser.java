package com.github.sir4ur0n.sonar.haskell.scannerside;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

import com.github.sir4ur0n.sonar.haskell.HLintError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.vavr.collection.List;
import io.vavr.control.Try;
import java.io.File;
import java.io.FileReader;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class HaskellLintAnalysisResultsParser {

  private static final Logger LOGGER = Loggers.get(HaskellLintAnalysisResultsParser.class);
  private static final Gson GSON = new Gson();

  /**
   * parse the report provided by hlint
   *
   * @param file hlintReport
   * @return List of Haskell Errors
   */
  public List<HLintError> parse(File file) {

    LOGGER.info("Parsing file {}", file.getAbsolutePath());

    return Try.withResources(() -> new FileReader(file))
        .of(reader -> GSON.fromJson(reader, JsonArray.class))
        .map(issues -> List.ofAll(issues)
            .map(issue -> (JsonObject) issue)
            .map(issue -> new HLintError()
                .withHint("hlint:" + issue.get("hint").getAsString())
                .withFilePath(issue.get("file").getAsString())
                .withLine(Integer.parseInt(issue.get("startLine").getAsString()))
                .withDescription(generateMessageForHints(
                    issue.get("hint").getAsString(),
                    issue.get("from").getAsString(),
                    issue.get("to").getAsString()))
            ))
        .getOrElseThrow(e -> new IllegalArgumentException("Failed to parse " + file, e));
  }


  private String generateMessageForHints(String hint, String from, String to) {
    return Match(hint).of(
        Case($("Reduce duplication"), "Reduce this code duplication: " + to),
        Case($("Redundant bracket"), "Remove unnecessary parentheses. Replace with: " + to),
        Case($("Use fewer imports"), "Use: " + to + " once only"),
        Case($(), "Expression found: " + from + ". Should be replaced with: " + to)
    );
  }
}
