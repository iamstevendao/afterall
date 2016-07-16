package fukie.afterall.items;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import fukie.afterall.activities.MainActivity;
import fukie.afterall.utils.Constants;
import fukie.afterall.utils.DatabaseProcess;
import fukie.afterall.utils.Events;

/**
 * Created by Fukie on 16/07/2016.
 */
public class SyncTask extends AsyncTask<Void, Void, Void> {
    private com.google.api.services.calendar.Calendar mService = null;
    private Exception mLastError = null;
    DatabaseProcess databaseProcess = new DatabaseProcess(MainActivity.context);
    MainActivity mainActivity;
    ProgressDialog mProgress = new ProgressDialog(MainActivity.context);
    HttpTransport transport = AndroidHttp.newCompatibleTransport();
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    int function;
    String syncId;
    int eventId;
    Events eventL;
    String calId;

    public SyncTask(GoogleAccountCredential credential) {
        mService = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("AfterAll")
                .build();
        this.function = Constants.TASK_SYNC;
    }

    public SyncTask(GoogleAccountCredential credential, String syncId, int id) {
        mService = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("AfterAll")
                .build();
        this.function = Constants.TASK_DELETE;
        this.syncId = syncId;
        this.eventId = id;
    }

    public SyncTask(GoogleAccountCredential credential, Events event, int function) {
        mService = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("AfterAll")
                .build();
        this.eventL = event;
        this.function = function;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            switch (function) {
                case Constants.TASK_SYNC:
                    makeSync();
                    break;
                case Constants.TASK_DELETE:
                    makeDelete();
                    break;
                case Constants.TASK_MODIFY:
                    makeModify();
                    break;
                case Constants.TASK_ADD:
                    makeAdd();
                    break;
            }
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
        }
        return null;
    }
    private void makeAdd() throws Exception{
        Event event = new Event()
                .setSummary(eventL.getName())
                .setLocation(String.valueOf(eventL.getLoop()))
                .setKind(String.valueOf(eventL.getKind()))
                .setDescription(String.valueOf(eventL.getImg()));

        DateTime startDateTime = new DateTime(eventL.getDate());
        EventDateTime start = new EventDateTime()
                .setDate(startDateTime)
                .setTimeZone(TimeZone.getDefault().getID());
        event.setStart(start);

        DateTime endDateTime = new DateTime(eventL.getDate());
        EventDateTime end = new EventDateTime()
                .setDate(endDateTime)
                .setTimeZone(TimeZone.getDefault().getID());
        event.setEnd(end);

        event = mService.events().insert(calId, event).execute();
        syncId = event.getId();
    }
    private void makeDelete() throws Exception{
        mService.events().delete(calId, syncId).execute();
    }

    private void makeModify() throws Exception{
        Event event = new Event()
                .setSummary(eventL.getName())
                .setLocation(String.valueOf(eventL.getLoop()))
                .setKind(String.valueOf(eventL.getKind()))
                .setDescription(String.valueOf(eventL.getImg()));

        DateTime startDateTime = new DateTime(eventL.getDate());
        EventDateTime start = new EventDateTime()
                .setDate(startDateTime)
                .setTimeZone(TimeZone.getDefault().getID());
        event.setStart(start);

        DateTime endDateTime = new DateTime(eventL.getDate());
        EventDateTime end = new EventDateTime()
                .setDate(endDateTime)
                .setTimeZone(TimeZone.getDefault().getID());
        event.setEnd(end);

        mService.events().update(calId, eventL.getIdSync(), event).execute();
    }

    private void makeSync() throws IOException {
        //DateTime now = new DateTime(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        // Iterate through entries in calendar list
        String pageToken = null;
        String calID = "";
        boolean isExisted = false;

        do {
            CalendarList calendarList = mService.calendarList().list()
                    .setPageToken(pageToken).execute();
            List<CalendarListEntry> items = calendarList.getItems();

            for (CalendarListEntry calendarListEntry : items) {
                if (calendarListEntry.getSummary().equals("AfterAllCal")) {
                    calID = calendarListEntry.getId();
                    isExisted = true;
                    break;
                }
            }
            pageToken = calendarList.getNextPageToken();
        } while (pageToken != null);

        if (!isExisted) {
            com.google.api.services.calendar.model.Calendar calendar = new Calendar();
            calendar.setSummary("AfterAllCal");
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeZone(tz.getID());
            Calendar created = mService.calendars().insert(calendar).execute();
            calID = created.getId();
        }
        //160715
        this.calId = calID;
        pageToken = null;
        com.google.api.services.calendar.model.Events events
                = mService.events().list(calID).setPageToken(pageToken).execute();
        List<Event> eventCloud = events.getItems();
        List<Events> eventLocal = databaseProcess.getAllEvent(-1);
        do {
            for (Event eventC : eventCloud) {
                boolean available = false;
                DateTime startx = eventC.getStart().getDate();
                for (Events eventL : eventLocal) {
                    if (eventC.getId().equals(eventL.getIdSync())) {
                        available = true;
                        if (eventL.getState() == Constants.EVENT_STATE_READ) {
                            databaseProcess.modifyEvent(eventL.getId()
                                    , eventC.getSummary()
                                    , Integer.parseInt(eventC.getKind())
                                    , sdf.format(startx)
                                    , Integer.parseInt(eventC.getLocation())
                                    , Integer.parseInt(eventC.getDescription())
                                    , Constants.EVENT_STATE_READ);
                        }
                        break;
                    }
                }
                if (!available) {
                    databaseProcess.insertEvent(eventC.getSummary()
                            , Integer.parseInt(eventC.getKind())
                            , sdf.format(startx)
                            , Integer.parseInt(eventC.getLocation())
                            , Integer.parseInt(eventC.getDescription())
                            , Constants.EVENT_STATE_READ
                            , eventC.getId()
                            , 0);
                }
            }
            pageToken = events.getNextPageToken();
        } while (pageToken != null);

        for (Events eventL : eventLocal) {
            if (eventL.getState() == Constants.EVENT_STATE_WRITE) {
                if (eventL.getDeleted() == 1) {
                    mService.events().delete(calID, eventL.getIdSync()).execute();
                    databaseProcess.deleteEvent(eventL.getId());
                } else {
                    Event event = new Event()
                            .setSummary(eventL.getName())
                            .setLocation(String.valueOf(eventL.getLoop()))
                            .setKind(String.valueOf(eventL.getKind()))
                            .setDescription(String.valueOf(eventL.getImg()));

                    DateTime startDateTime = new DateTime(eventL.getDate());
                    EventDateTime start = new EventDateTime()
                            .setDate(startDateTime)
                            .setTimeZone(TimeZone.getDefault().getID());
                    event.setStart(start);

                    DateTime endDateTime = new DateTime(eventL.getDate());
                    EventDateTime end = new EventDateTime()
                            .setDate(endDateTime)
                            .setTimeZone(TimeZone.getDefault().getID());
                    event.setEnd(end);

                    boolean available = false;
                    for (Event eventC : eventCloud) {
                        if (eventC.getId().equals(eventL.getIdSync())) {
                            available = true;
                            mService.events().update(calID, eventC.getId(), event).execute();
                        }
                    }
                    if (!available) {
                        event = mService.events().insert(calID, event).execute();
                    }
                    databaseProcess.updateStateAndSyncId(eventL.getId()
                            , Constants.EVENT_STATE_READ
                            , event.getId());
                }
            }
        }
    }


    @Override
    protected void onPreExecute() {
        mProgress.setMessage("Syncing...");
        mProgress.show();
    }

    @Override
    protected void onPostExecute(Void output) {
        mProgress.hide();
        switch (function) {
            case Constants.TASK_SYNC:
                mainActivity.sharedPreferences.edit().putBoolean(MainActivity.IS_USE_SYNC, true).apply();
                break;
            case Constants.TASK_DELETE:
                databaseProcess.deleteEvent(eventId);
                break;
            case Constants.TASK_MODIFY:
                databaseProcess.updateState(eventL.getId());
                break;
            case Constants.TASK_ADD:
                databaseProcess.updateStateAndSyncId(eventL.getId()
                    , Constants.EVENT_STATE_READ
                    , syncId);
                break;
        }
//        if (output == null || output.size() == 0) {
//            //  mOutputText.setText("No results returned.");
//        } else {
//            output.add(0, "Data retrieved using the Google Calendar API:");
//            //   mOutputText.setText(TextUtils.join("\n", output));
//        }
    }

    @Override
    protected void onCancelled() {
        mProgress.hide();
        if (mLastError != null) {
            if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                mainActivity.showGooglePlayServicesAvailabilityErrorDialog(
                        ((GooglePlayServicesAvailabilityIOException) mLastError)
                                .getConnectionStatusCode());
            } else if (mLastError instanceof UserRecoverableAuthIOException) {
                mainActivity.startActivityForResult(
                        ((UserRecoverableAuthIOException) mLastError).getIntent(),
                        MainActivity.REQUEST_AUTHORIZATION);
            } else {
                //  mOutputText.setText("The following error occurred:\n"
                //   + mLastError.getMessage());
            }
        } else {
            //  mOutputText.setText("Request cancelled.");
        }
    }
}
