package com.summerizer.videoSummerizer;

import com.summerizer.videoSummerizer.Service.ResumeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class VideoSummerizerApplicationTests {

	@Autowired
	private ResumeService resume;

	@Test
	void contextLoads() throws IOException {

		resume.generateResumeResponse("I am manish kumar student in Ajay kumar garg engineering college ghaziabad pursuing third year of my B.tech and a java developer");
	}

}
