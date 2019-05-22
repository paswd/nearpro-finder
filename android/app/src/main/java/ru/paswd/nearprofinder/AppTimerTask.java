package ru.paswd.nearprofinder;

import java.util.TimerTask;

import ru.paswd.nearprofinder.model.Session;

public class AppTimerTask extends TimerTask {

    private Session session;

    public AppTimerTask(Session _session) {
        session = _session;
    }

    @Override
    public void run() {
        session.check();
    }
}
