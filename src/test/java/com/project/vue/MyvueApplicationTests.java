package com.project.vue;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.project.vue.common.Constants;

//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class MyvueApplicationTests {
	
	@Autowired
	MockMvc mockMvc;

	@Test
	void contextLoads() {
	}
	
	@Test
	public void BoardTest() throws Exception {
		mockMvc.perform(get(Constants.REQUEST_MAPPING_PREFIX+"/board") // MockMvc 에 요청을 보냄
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()) // 예상값 검증
                .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].userId", is("49alswjd")))
                .andDo(print());
	}

}
