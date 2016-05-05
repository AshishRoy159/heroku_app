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

import com.mindfire.bicyclesharing.component.UserComponent;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.service.UserService;

public class UserServiceTest {
	
	@InjectMocks
	private UserService userService;
	
	@Mock
	private UserComponent userComponent;
	
	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testFetchUserDetails(){
		User user = new User();
		user.setUserId(1l);
		
		when(userComponent.getUser(1l)).thenReturn(user);
				
		User testUser = userService.userDetails(1l);
		verify(userComponent).getUser(1l);
//		assertEquals(1l, testUser.getUserId().longValue());
		
		assertThat(testUser.getUserId(), is(1l));
	}

}
