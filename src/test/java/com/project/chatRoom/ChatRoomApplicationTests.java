package com.project.chatRoom;

import com.project.chatRoom.model.Message;
import com.project.chatRoom.model.User;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChatRoomApplicationTests {
	@Test
	public void testContent(){
		Message m= new Message();
		m.setMsg("Hello, How are you?");
		Assert.assertEquals("Hello",m.getMsg());
	}
	@Test
	public void testMessage(){
		User us = new User();
		us.setName("Rohit");
		Message m = new Message();
		m.setUsername(us.getName());
		Assert.assertEquals(us.getName(), m.getUsername());
	}

}
