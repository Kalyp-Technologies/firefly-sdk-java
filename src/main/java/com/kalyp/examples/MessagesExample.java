package com.kalyp.examples;

import com.kalyp.firefly.Firefly;
import com.kalyp.firefly.model.Identity;

import java.util.List;

public class MessagesExample {

    public static void main(String[] args) {
        Firefly firefly = new Firefly("http://localhost:5000/api/v1");
        firefly.sendBroadcast("hello world");

        List<Identity> orgs = firefly.listOrganizations();
        System.out.println("List of orgs:" + orgs);

        firefly.sendPrivateMessage("this is private message", orgs.get(0).getDid());
    }
}
