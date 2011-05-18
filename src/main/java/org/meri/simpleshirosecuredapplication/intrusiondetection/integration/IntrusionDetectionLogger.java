package org.meri.simpleshirosecuredapplication.intrusiondetection.integration;

import org.owasp.appsensor.ASLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntrusionDetectionLogger implements ASLogger {

  private static final Logger log = LoggerFactory.getLogger(IntrusionDetectionLogger.class);

	@Override
  public void fatal(String message) {
		log.error(message);
  }

	@Override
  public void fatal(String message, Throwable throwable) {
		log.error(message, throwable);
  }

	@Override
  public void error(String message) {
		log.error(message);
  }

	@Override
  public void error(String message, Throwable throwable) {
		log.error(message, throwable);
  }

	@Override
  public void warning(String message) {
		log.warn(message);
  }

	@Override
  public void warning(String message, Throwable throwable) {
		log.warn(message, throwable);
  }

	@Override
  public void info(String message) {
		log.info(message);
  }

	@Override
  public void info(String message, Throwable throwable) {
		log.info(message, throwable);
  }

	@Override
  public void debug(String message) {
		log.debug(message);
  }

	@Override
  public void debug(String message, Throwable throwable) {
		log.debug(message, throwable);
  }

}
