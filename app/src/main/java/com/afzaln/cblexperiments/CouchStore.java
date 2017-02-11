package com.afzaln.cblexperiments;

import android.content.Context;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.View;
import com.couchbase.lite.android.AndroidContext;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by afzal on 2017-02-11.
 */

public class CouchStore {

    private static CouchStore INSTANCE;
    private Manager manager;
    private Database database;
    private View alphaView;
    private View betaView;

    public static CouchStore getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CouchStore(context);
        }

        return INSTANCE;
    }

    private CouchStore(Context context) {
        AndroidContext androidContext = new AndroidContext(context.getApplicationContext());
        try {
            manager = new Manager(androidContext, Manager.DEFAULT_OPTIONS);
            database = manager.getDatabase("catalog");
        } catch (IOException e) {
            Timber.e("Manager not created");
            return;
        } catch (CouchbaseLiteException e) {
            Timber.e("Database not created");
            return;
        }

        alphaView = database.getView("alphas");
        alphaView.setMap((document, emitter) -> {
            if (document.get("type").equals("alpha")) {
                emitter.emit(document.get("created_at"), null);
            }
        }, "1");

        betaView = database.getView("betas");
        betaView.setMapReduce((document, emitter) -> {
            if (document.get("type").equals("beta")) {
                emitter.emit(document.get("num"), null);
            }
        }, (keys, values, rereduce) -> {
            int length = keys.size();
            int sum = 0;
            for (int i = 0; i < keys.size(); i++) {
                sum += (int) keys.get(i);
            }

            double avg = (double) sum / length;
            return avg; // get avg
//            return keys.size(); // get count

            // todo get highest frequency
        }, "3");
    }

    public void createDoc(Doc doc) {
        Document document = database.createDocument();
        Map<String, Object> alphaMap = doc.toHashMap();

        try {
            document.putProperties(alphaMap);
        } catch (CouchbaseLiteException e) {
            Timber.e(e, "Could not create alpha doc");
        }
    }

    /**
     * Gets Alpha objects between two dates
     */
    public void getAlphas() {
        Query query = alphaView.createQuery();
        Calendar startCal = Calendar.getInstance();
        startCal.set(2017, 1, 11, 5, 10, 10);

        Calendar endCal = Calendar.getInstance();
        endCal.set(2017, 1, 11, 5, 18, 10);

        String startDate = Utils.sDateFormatter.format(startCal.getTime());
        String endDate = Utils.sDateFormatter.format(endCal.getTime());
        query.setStartKey(startDate);
        query.setEndKey(endDate);
        try {
            QueryEnumerator run = query.run();
            Timber.d("Found " + run.getCount() + " results");
            for (QueryRow queryRow : run) {
                Document document = queryRow.getDocument();
                Map<String, Object> properties = document.getProperties();
                Alpha alpha = Alpha.fromMap(properties);
                Timber.d("created at: " + alpha.created_at);
            }
        } catch (CouchbaseLiteException e) {
            Timber.e(e, "Could not get alphas");
        }
    }

    /**
     * Gets the average of beta objects
     * You can also specify which values to restrict this to
     */
    public void getBetas() {
        Query query = betaView.createQuery();
//        query.setStartKey(5);
//        query.setEndKey(6);
        try {
            QueryEnumerator run = query.run();
            for (QueryRow queryRow : run) {
                double value = (double) queryRow.getValue();
                Map<String, Object> map = queryRow.asJSONDictionary();
                Timber.d("Got beta avg: " + value);
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

    }
}
