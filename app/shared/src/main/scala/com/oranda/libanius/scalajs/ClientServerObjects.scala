/*
 * Libanius
 * Copyright (C) 2012-2015 James McCabe <james@oranda.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.oranda.libanius.scalajs

import com.oranda.libanius.model.quizitem.QuizItemViewWithChoices

case class DataToClient(quizItemReact: Option[QuizItemReact], scoreText: String)

// Note: for promptResponseMap, ListMap does not work with upickle
case class QuizItemReact(prompt: String, correctResponse: String,
    promptType: String, responseType: String, numCorrectResponsesInARow: Int,
    promptResponseMap: Seq[(String, String)]) {

  def allChoices: Iterable[String] = promptResponseMap.map(_._1)
}

object QuizItemReact {

  // Should be apply, but upickle complains.
  def construct(qi: QuizItemViewWithChoices, promptResponseMap: Seq[(String, String)]):
      QuizItemReact =
    QuizItemReact(qi.prompt, qi.correctResponse, qi.promptType, qi.responseType,
        qi.numCorrectResponsesInARow, promptResponseMap)
}

abstract class RequestToServer(val userToken: String)

case class QuizItemAnswer(override val userToken: String, prompt: String, correctResponse: String,
    promptType: String, responseType: String, response: String)
  extends RequestToServer(userToken) {

  def isCorrect = correctResponse == response
}

object QuizItemAnswer {

  // Should be apply, but upickle complains.
  def construct(userToken: String, qi: QuizItemReact, choice: String): QuizItemAnswer =
    QuizItemAnswer(userToken, qi.prompt, qi.correctResponse, qi.promptType,
        qi.responseType, choice)
}