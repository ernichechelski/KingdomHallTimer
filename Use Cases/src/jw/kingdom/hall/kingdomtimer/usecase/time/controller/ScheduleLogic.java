package jw.kingdom.hall.kingdomtimer.usecase.time.controller;

import jw.kingdom.hall.kingdomtimer.entity.task.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * All rights reserved & copyright ©
 */
public abstract class ScheduleLogic implements ScheduleController {

    private final List<Task> schedule = new ArrayList<>();

    @Override
    public void clear() {
        schedule.clear();
        onListChange(schedule);
    }

    @Override
    public void setTasks(List<Task> list) {
        schedule.clear();
        schedule.addAll(list);
        onListChange(schedule);
    }

    @Override
    public void addTask(Task task) {
        schedule.add(task);
        onListChange(schedule);
    }

    @Override
    public void removeTask(String id) {
        try {
            Task task = getTaskForId(id);
            schedule.remove(task);
            onListChange(schedule);
        } catch (TaskNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Task> getTasks() {
        return schedule;
    }

    @Override
    public void moveTask(String id, int index) {
        try {
            Task original = getTaskForId(id);
            int startIndex = schedule.indexOf(original);
            Collections.swap(schedule, startIndex, index);
            onListChange(schedule);
        } catch (TaskNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Task bringOutFirstTask() {
        Task task = schedule.get(0);
        schedule.remove(0);
        onListChange(schedule);
        return task;
    }

    protected abstract void onListChange(List<Task> schedule);

    private Task getTaskForId(String id) throws TaskNotFoundException {
        if(id!=null) {
            for(Task task:getTasks()) {
                if(task.getID().equals(id)) {
                    return task;
                }
            }
        }
        throw new TaskNotFoundException(id);
    }
}
