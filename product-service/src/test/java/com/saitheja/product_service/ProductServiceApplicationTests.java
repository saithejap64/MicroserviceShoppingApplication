package com.saitheja.product_service;

import org.springframework.boot.test.context.SpringBootTest;




@SpringBootTest

class ProductServiceApplicationTests {
//	@Container
//	static MySQLContainer mySQLContainer= new MySQLContainer("mysql");
//
//	@Autowired
//	private MockMvc mockMvc;
//
//	@Autowired
//	private ObjectMapper objectMapper;
//
//	@DynamicPropertySource
//	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
//		dynamicPropertyRegistry.add("spring.datasource.url",mySQLContainer::getJdbcUrl);
//	}
//
//
//	@Test
//	void shouldCreateProduct() throws Exception {
//		ProductRequestDTO productRequestDTO=getProductRequest();
//		String productRequestString=objectMapper.writeValueAsString(productRequestDTO);
//		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(productRequestString))
//				.andExpect(status().isCreated());
//	}
//
//	private ProductRequestDTO getProductRequest(){
//		return ProductRequestDTO.builder()
//				.name("iPhone 13")
//				.description("iPhone 13")
//				.price(BigDecimal.valueOf(49000))
//				.build();
//	}

}
