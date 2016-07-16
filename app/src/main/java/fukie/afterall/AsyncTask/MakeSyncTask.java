package fukie.afterall.AsyncTask;

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
import java.util.ArrayList;
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
public class MakeSyncTask extends AsyncTask<Void, Void, List<String>> {
    private com.google.api.services.calendar.Calendar mService = null;
    private Exception mLastError = null;
    DatabaseProcess databaseProcess = new DatabaseProcess(MainActivity.context);
    ProgressDialog mProgress;
    MainActivity mainActivity;

    public MakeSyncTask(GoogleAccountCredential credential, ProgressDialog mProgress) {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("AfterAll")
                .build();
        this.mProgress = mProgress;
    }

    @Override
    protected List<String> doInBackground(Void... params) {
        try {
            return getDataFromApi();
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    private List<String> getDataFromApi() throws IOException {
        //DateTime now = new DateTime(System.currentTimeMillis());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        List<String> eventStrings = new ArrayList<>();

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
                    //if(!eventL.getIdSync().equals(""))
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
        return eventStrings;
    }


    @Override
    protected void onPreExecute() {
        mProgress.show();
    }

    @Override
    protected void onPostExecute(List<String> output) {
        mProgress.hide();
        if (output == null || output.size() == 0) {
            //  mOutputText.setText("No results returned.");
        } else {
            output.add(0, "Data retrieved using the Google Calendar API:");
            //   mOutputText.setText(TextUtils.join("\n", output));
        }
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
