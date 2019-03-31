package domain

import java.time.Instant


data class IssueUpdate @Throws(IllegalArgumentException::class) constructor(val state: State? = null, val comment: String? = null) {
  
  val timestamp: Instant = Instant.now()
  
  init {
    if (state == null && comment == null) throw IllegalArgumentException("Both state and comment can't be null")
  }
  
  
}