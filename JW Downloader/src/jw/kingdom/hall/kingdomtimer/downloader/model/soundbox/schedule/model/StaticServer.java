/*
 * Created 05.09.18 02:57.
 * Last modified 05.09.18 02:54
 * This file is part of KingdomHallTimer which is released under "no licence".
 */

package jw.kingdom.hall.kingdomtimer.downloader.model.soundbox.schedule.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jw.kingdom.hall.kingdomtimer.downloader.model.soundbox.schedule.entity.Meeting;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * This file is part of KingdomHallTimer which is released under "no licence".
 */
class StaticServer {
    static List<Meeting> getMeetings(String url){
        String content = getPageContent(url);
        Type type = new TypeToken<List<Meeting>>() {}.getType();
        return new Gson().fromJson(content, type);
    }

    private static String getPageContent(String url) {
        try {
            return WebPageUtils.download(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "[]";
    }
}
