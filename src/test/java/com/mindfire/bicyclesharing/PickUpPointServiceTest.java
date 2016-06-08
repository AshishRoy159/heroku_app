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

import com.mindfire.bicyclesharing.component.PickUpPointComponent;
import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.service.PickUpPointService;

public class PickUpPointServiceTest {
	
	@InjectMocks
	private PickUpPointService pickUpPointService;
	
	@Mock
	private PickUpPointComponent pickUpPointComponent;
	
	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testFetchUserDetails(){
		PickUpPoint pickUpPoint = new PickUpPoint();
		pickUpPoint.setPickUpPointId(1);
		
		when(pickUpPointComponent.findPickUpPointById(1)).thenReturn(pickUpPoint);
				
		PickUpPoint testPickUpPoint = pickUpPointService.getPickupPointById(1);
		verify(pickUpPointComponent).findPickUpPointById(1);
//		assertEquals(1l, testUser.getUserId().longValue());
		
		assertThat(testPickUpPoint.getPickUpPointId(), is(1));
	}

}
