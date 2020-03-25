package com.project.chatRoom.controller;
import com.google.gson.Gson;
import com.project.chatRoom.model.Message;
import com.project.chatRoom.model.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket Server
 *
 * @see ServerEndpoint WebSocket Client
 * @see Session   WebSocket Session
 */
@Controller
@Component
@ServerEndpoint("/chat/{username}")
public class WebSocketChatServer {

    /**
     * All chat sessions.
     */
    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();
    @RequestMapping("/login")
    public String LoginController(){
        System.out.println("In Login");
        return "login";
    }
    @RequestMapping(value = "/chat/{username}", method = RequestMethod.GET)
//    @ResponseBody
    public String ChatController(Model model, @PathVariable("username") String username) {
        System.out.println("In Chat "+username);
        User user = new User();
        user.setName(username);
        model.addAttribute("username", user.getName());
        System.out.println("In use getName "+user.getName());
//        ses.setAttribute("username", user.getName());
        return "chat";
    }
    private static void sendMessageToAll(String msg) {
        //TODO: add send message method.
        Gson gson = new Gson();
        onlineSessions.forEach((k,v)->{
                    try {
                        v.getBasicRemote().sendText(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                );
    }

    /**
     * Open connection, 1) add session, 2) add user.
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String user) {
        //TODO: add on open connection
        System.out.println("In open Session "+user);
//        config.getUserProperties().put("user", user);
        System.out.println(session.getUserProperties().put("username", user));
        System.out.println(session.getId()+ " In get Session props" + session.getUserProperties().get("username"));
        String name = session.getUserProperties().get("username").toString();
        onlineSessions.put(session.getId(), session);
//        sendMessageToAll();
    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        //TODO: add send message.
        System.out.println(session.getId()+" "+ jsonStr);
        Gson gson = new Gson();
        Message msg = gson.fromJson(jsonStr, Message.class);
        System.out.println(msg.getType());
        msg.setType("SPEAK");
        msg.setOnlineCount(onlineSessions.size());
        System.out.println(msg.getUsername()+" "+msg.getMsg()+" "+msg.getType()+" "+msg.getOnlineCount());
        String json = gson.toJson(msg);
        System.out.println("Final json "+json);
        sendMessageToAll(json);
    }

    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(Session session) {
        //TODO: add close connection.
    }

    /**
     * Print exception.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

}
