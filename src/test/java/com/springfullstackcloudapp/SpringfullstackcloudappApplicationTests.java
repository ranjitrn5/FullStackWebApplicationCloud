package com.springfullstackcloudapp;

import com.springfullstackcloudapp.backend.service.I18NService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringfullstackcloudappApplicationTests {
	@Autowired
	I18NService i18NService;

	@Test
	public void testMessageByLocaleService () throws Exception{
		String expectedResult = "Bootstrap starter template";
		String messageId = "index.main.callout";
		String actual = i18NService.getMessage(messageId);
		Assert.assertEquals("The actual and expected string don't match", expectedResult, actual);
	}



}
