package fukie.afterall.items;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
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
import java.util.List;
import java.util.TimeZone;

import fukie.afterall.activity.MainActivity;
import fukie.afterall.utils.Constants;
import fukie.afterall.utils.DatabaseProcess;
import fukie.afterall.utils.Events;

/**
 * Created by Fukie on 18/07/2016.
 */
public class SyncTask extends AsyncTask<Void, Void, Void> {
    private com.google.api.services.calendar.Calendar mService = null;
    private Exception mLastError = null;
    private DatabaseProcess databaseProcess = new DatabaseProcess(MainActivity.context);
    private HttpTransport transport = AndroidHttp.newCompatibleTransport();
    private JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    private int function;
    private String syncId;
    private Events eventL;
    private static String calId;
    private Activity activity;
    private ProgressDialog progressDialog;

    public SyncTask(Activity activity) {
        this.function = Constants.TASK_SYNC;
        this.activity = activity;
    }

    public SyncTask(String syncId, Activity activity) {
        this.function = Constants.TASK_DELETE;
        this.syncId = syncId;
        this.activity = activity;
    }

    public SyncTask(Events event, int function, Activity activity) {
        this.eventL = event;
        this.function = function;
        this.activity = activity;
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

    private void makeAdd() throws Exception {
        Event event = new Event()
                .setSummary(eventL.getName())
                .setLocation(String.valueOf(eventL.getLoop()) +
                        String.valueOf(eventL.getKind()))
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

    private void makeDelete() throws Exception {
        mService.events().delete(calId, syncId).execute();
    }

    private void makeModify() throws Exception {
        Event event = new Event()
                .setSummary(eventL.getName())
                .setLocation(String.valueOf(eventL.getLoop()) +
                        String.valueOf(eventL.getKind()))
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
        String pageToken = null;
        String calID = "";
        boolean isExisted = false;

        do {
            CalendarList calendarList = mService.calendarList().list()
                    .setPageToken(pageToken).execute();
            List<CalendarListEntry> items = calendarList.getItems();

            for (CalendarListEntry calendarListEntry : items) {
                if (calendarListEntry.getId()
                        .equals(MainActivity.sharedPreferences.getString(MainActivity.CAL_ID, ""))) {
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
            MainActivity.sharedPreferences.edit().putString(MainActivity.CAL_ID, calID).apply();
        }
        //160715
        calId = calID;
        pageToken = null;
        com.google.api.services.calendar.model.Events events
                = mService.events().list(calID).setPageToken(pageToken).execute();
        List<Event> eventCloud = events.getItems();
        List<Events> eventLocal = databaseProcess.getAllEvent(-1);
        do {
            for (Event eventC : eventCloud) {
                boolean available = false;
                DateTime startx = eventC.getStart().getDateTime();
                if (startx == null) {
                    startx = eventC.getStart().getDate();
                }
                for (Events eventL : eventLocal) {
                    if (eventC.getId().equals(eventL.getIdSync())) {
                        available = true;
                        if (eventL.getState() == Constants.EVENT_STATE_READ) {
                            databaseProcess.modifyEvent(true
                                    , eventL.getId()
                                    , eventC.getSummary()
                                    , Character.getNumericValue(eventC.getLocation().charAt(1))
                                    , startx.toString()
                                    , Character.getNumericValue(eventC.getLocation().charAt(0))
                                    , Integer.parseInt(eventC.getDescription())
                                    , Constants.EVENT_STATE_READ);
                        }
                        break;
                    }
                }
                if (!available) {
                    databaseProcess.insertEvent(eventC.getSummary()
                            , Character.getNumericValue(eventC.getLocation().charAt(1))
                            , startx.toString()
                            , Character.getNumericValue(eventC.getLocation().charAt(0))
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
                    for (Event eventC : eventCloud) {
                        if (eventC.getId().equals(eventL.getIdSync())) {
                            mService.events().delete(calID, eventL.getIdSync()).execute();
                            break;
                        }
                    }
                    databaseProcess.deleteEvent(eventL.getId());
                } else {
                    Event event = new Event()
                            .setSummary(eventL.getName())
                            .setLocation(String.valueOf(eventL.getLoop())
                                    + String.valueOf(eventL.getKind()))
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
            } else {
                boolean deletedInCloud = true;
                for (Event eventC : eventCloud) {
                    if (eventC.getId().equals(eventL.getIdSync())) {
                        deletedInCloud = false;
                        break;
                    }
                }
                if (deletedInCloud) {
                    databaseProcess.deleteEvent(eventL.getId());
                }
            }
        }
    }

    @Override
    protected void onPostExecute(Void output) {
        switch (function) {
            case Constants.TASK_SYNC:
                List<Events> events = databaseProcess.getAllEvent(-1);
                MainActivity.recyclerAdapter2.updateData(MainActivity.rearrangeList(events));
                break;
            case Constants.TASK_DELETE:
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
        progressDialog.hide();
    }

    @Override
    protected void onCancelled() {
        progressDialog.hide();
        if (mLastError != null) {
            if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
                Dialog dialog = apiAvailability.getErrorDialog(
                        activity,
                        ((GooglePlayServicesAvailabilityIOException) mLastError)
                                .getConnectionStatusCode(),
                        MainActivity.REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            } else if (mLastError instanceof UserRecoverableAuthIOException) {
                activity.startActivityForResult(
                        ((UserRecoverableAuthIOException) mLastError).getIntent(),
                        MainActivity.REQUEST_AUTHORIZATION);
            } else {
                Toast.makeText(MainActivity.context, "The following error occurred: "
                        + mLastError.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println(mLastError.getMessage());
            }
        } else {
            Toast.makeText(MainActivity.context, "Request cancelled.", Toast.LENGTH_LONG).show();
        }
    }

    protected void onPreExecute() {
        calId = MainActivity.sharedPreferences.getString(MainActivity.CAL_ID, "");
        mService = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, MainActivity.mCredential)
                .setApplicationName("AfterAll")
                .build();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Syncing...");
        progressDialog.show();
    }
}
