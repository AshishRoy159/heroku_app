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

import com.mindfire.bicyclesharing.component.TransferRequestComponent;
import com.mindfire.bicyclesharing.model.TransferRequest;
import com.mindfire.bicyclesharing.service.TransferRequestService;

public class TransferRequestServiceTest {
	
	@InjectMocks
	private TransferRequestService transferRequestService;
	
	@Mock
	private TransferRequestComponent transferRequestComponent;
	
	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testFetchUserDetails(){
		TransferRequest transferRequest = new TransferRequest();
		transferRequest.setRequestId(1l);
		
		when(transferRequestComponent.getTransferRequest(1l)).thenReturn(transferRequest);
				
		TransferRequest testTransferRequest = transferRequestService.findTransferRequest(1l);
		verify(transferRequestComponent).getTransferRequest(1l);
//		assertEquals(1l, testUser.getUserId().longValue());
		
		assertThat(testTransferRequest.getRequestId(), is(1l));
	}

}
