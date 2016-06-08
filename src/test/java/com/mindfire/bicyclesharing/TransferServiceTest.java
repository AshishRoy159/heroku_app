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

import com.mindfire.bicyclesharing.component.TransferComponent;
import com.mindfire.bicyclesharing.model.Transfer;
import com.mindfire.bicyclesharing.service.TransferService;

public class TransferServiceTest {
	
	@InjectMocks
	private TransferService transferService;
	
	@Mock
	private TransferComponent transferComponent;
	
	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testFetchUserDetails(){
		Transfer transfer = new Transfer();
		transfer.setTransferId(1l);
		
		when(transferComponent.getTransferDetails(1l)).thenReturn(transfer);
				
		Transfer testTransfer = transferService.findTransferDetails(1l);
		verify(transferComponent).getTransferDetails(1l);
//		assertEquals(1l, testUser.getUserId().longValue());
		
		assertThat(testTransfer.getTransferId(), is(1l));
	}

}
