package com.cplier.diameter.exception

open class DiameterException(
  val errorReason: Int,
  message: String?,
  cause: Throwable? = null,
  val exceptionType: Int = DIAMETER_UNSPECIFIED_EXCEPTION
) : Exception(message, cause) {

  companion object {
    const val DIAMETER_UNSPECIFIED_EXCEPTION = 0
    const val DIAMETER_BUILD_EXCEPTION = 1
    const val DIAMETER_PARSE_EXCEPTION = 2
    const val DIAMETER_DICTIONARY_EXCEPTION = 3
  }

}
