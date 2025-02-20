/*
 * SonarQube Java
 * Copyright (C) 2012-2022 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.java.checks;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import org.sonar.check.Rule;
import org.sonar.java.checks.helpers.ReassignmentFinder;
import org.sonar.java.model.LiteralUtils;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.semantic.MethodMatchers;
import org.sonar.plugins.java.api.tree.BinaryExpressionTree;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.Tree;

@Rule(key = "S2692")
public class IndexOfWithPositiveNumberCheck extends IssuableSubscriptionVisitor {

  private static final String JAVA_LANG_STRING = "java.lang.String";
  private static final String INDEX_OF = "indexOf";

  private static final MethodMatchers CHECKED_METHODS = MethodMatchers.or(
    MethodMatchers.create()
      .ofTypes(JAVA_LANG_STRING)
      .names(INDEX_OF)
      .addParametersMatcher("int")
      .addParametersMatcher(JAVA_LANG_STRING)
      .build(),
    MethodMatchers.create()
      .ofSubTypes("java.util.List")
      .names(INDEX_OF)
      .addParametersMatcher("java.lang.Object")
      .build());

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return Arrays.asList(Tree.Kind.GREATER_THAN, Tree.Kind.LESS_THAN);
  }

  @Override
  public void visitNode(Tree tree) {
    BinaryExpressionTree binaryTree = (BinaryExpressionTree) tree;
    if (tree.is(Tree.Kind.GREATER_THAN)) {
      checkForIssue(tree, binaryTree.leftOperand(),
        LiteralUtils.longLiteralValue(retrieveClosestAssignmentIfAny(binaryTree.rightOperand())));
    } else {
      checkForIssue(tree, binaryTree.rightOperand(),
        LiteralUtils.longLiteralValue(retrieveClosestAssignmentIfAny(binaryTree.leftOperand())));
    }
  }

  private static ExpressionTree retrieveClosestAssignmentIfAny(ExpressionTree expression) {
    if (expression.is(Tree.Kind.IDENTIFIER)) {
      IdentifierTree identifier = (IdentifierTree) expression;
      ExpressionTree reassignmentOrDeclaration = ReassignmentFinder.getClosestReassignmentOrDeclarationExpression(expression,
        identifier.symbol());
      if (reassignmentOrDeclaration != null) {
        return reassignmentOrDeclaration;
      }
    }
    return expression;
  }

  private void checkForIssue(Tree tree, ExpressionTree operand, @Nullable Long constant) {
    if (constant != null && constant == 0 && isIndexOfOnArrayOrString(operand)) {
      reportIssue(tree, "0 is a valid index, but is ignored by this check.");
    }
  }

  private static boolean isIndexOfOnArrayOrString(Tree tree) {
    return tree.is(Tree.Kind.METHOD_INVOCATION) && CHECKED_METHODS.matches((MethodInvocationTree) tree);
  }

}
