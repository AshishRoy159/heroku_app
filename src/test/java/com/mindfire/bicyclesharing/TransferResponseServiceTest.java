package com.mindfire.bicyclesharing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mindfire.bicyclesharing.component.TransferResponseComponent;
import com.mindfire.bicyclesharing.model.TransferResponse;
import com.mindfire.bicyclesharing.service.TransferResponseService;

public class TransferResponseServiceTest {
	
	@InjectMocks
	private TransferResponseService transferResponseService;
	
	@Mock
	private TransferResponseComponent transferResponseComponent;
	
	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testFetchUserDetails(){
		TransferResponse transferResponse = new TransferResponse();
		transferResponse.setResponseId(1l);
		
		when(transferResponseComponent.getTransferResponse(1l)).thenReturn(transferResponse);
				
		TransferResponse testTransferResponse = transferResponseService.findResponseById(1l);
		verify(transferResponseComponent).getTransferResponse(1l);
//		assertEquals(1l, testUser.getUserId().longValue());
		
		assertThat(testTransferResponse.getResponseId(), is(1l));
	}

}
