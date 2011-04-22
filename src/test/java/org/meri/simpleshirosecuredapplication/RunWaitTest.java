package org.meri.simpleshirosecuredapplication;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;
import org.meri.simpleshirosecuredapplication.test.AbstractContainerTest;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

public class RunWaitTest extends AbstractContainerTest {

	@Test
	public void sleepForever() throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException {
		while (true)
			Thread.sleep(9999);
	}

}
