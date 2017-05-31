package com.example.async;

import java.util.List;

/**
 * Created by asus-Iabx on 2017/5/14.
 */
public interface EventHandler {
    void doHandle(EventModel model);
    List<EventType> getSupportEventTypes();
}
