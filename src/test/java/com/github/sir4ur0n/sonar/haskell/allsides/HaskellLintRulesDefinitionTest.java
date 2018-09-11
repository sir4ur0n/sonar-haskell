package com.github.sir4ur0n.sonar.haskell.allsides;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinition.Param;
import org.sonar.api.server.rule.RulesDefinition.Rule;

@ExtendWith(MockitoExtension.class)
class HaskellLintRulesDefinitionTest {

  private RulesDefinition.Repository repository;

  @BeforeEach
  void setUp() {
    HaskellLintRulesDefinition cut = new HaskellLintRulesDefinition();
    RulesDefinition.Context context = new RulesDefinition.Context();
    cut.define(context);
    repository = context.repository("haskell-haskelllint");
  }

  @Test
  void testRepository() {
    assertThat(repository.name()).isEqualTo("haskell-HaskellLint");
    assertThat(repository.language()).isEqualTo("haskell");
  }

  @Test
  void testRules() {
    Rule rule = repository.rule("hlint:Use putStr");

    assertThat(rule).isNotNull();
    assertThat(rule.name()).isEqualTo("Use putStr");
    assertThat(rule.type()).isEqualTo(RuleType.BUG);
    for (Rule r : repository.rules()) {
      for (Param param : r.params()) {
        assertThat(param.description()).isNotEmpty();
      }
    }

  }

}
