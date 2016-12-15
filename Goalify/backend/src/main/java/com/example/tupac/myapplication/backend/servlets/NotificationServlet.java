package com.example.tupac.myapplication.backend.servlets;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.*;

/**
 * Created by tupac on 12/12/2016.
 */

public class NotificationServlet extends HttpServlet {

    String SERVER_KEY = "AAAALHg9Clg:APA91bEFAgnZ-6Ch1J9e49k68SlJswfw8hBBwKHfxG8vQYsVzaKVwyx_tpazifVLxLjjtmv-RN4hcbEuG9W7Bgsa0hltpeuYQbJIq6euO2E7QZGQK2_fZ3m13m1OXfosHTAtOEtIwmYciB0xnPIMwZ0JQhDdzTe8SA";

    private static final Logger _logger = Logger.getLogger(NotificationServlet.class.getName());

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String strCallResult = "";
        resp.setContentType("text/plain");
        try {

            String teamName = req.getParameter("teamName");
            _logger.info("Got a team name to send notification : " + teamName);

            //
            // PUT TASK CODE HERE
            //
            resp.getWriter().println("*********************post****************************");


            //issue a post request
            HttpClient httpclient = HttpClientBuilder.create().build();
            HttpPost httppost = new HttpPost("https://fcm.googleapis.com/fcm/send");

            //add headers
            httppost.addHeader("Authorization", "key=" + SERVER_KEY);
            httppost.addHeader("Content-Type", "application/json");

            //create json body
            JSONObject notification = new JSONObject();
            notification.put("title", "Chelsea vs Arsenal");
            notification.put("body", "Match begins now!!!");

            JSONObject body = new JSONObject();
            body.put("to", "/topics/Chelsea");
            body.put("notification", notification);

            StringEntity params = new StringEntity(body.toString());

            //add the body
            httppost.setEntity(params);

            //Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    // do something useful
                    //resp.getWriter().println(instream.toString());
                } finally {
                    instream.close();
                }
            }


            strCallResult = "SUCCESS: notification sent";
            _logger.info(strCallResult);
            resp.getWriter().println(strCallResult);
        }
        catch (Exception ex) {
            strCallResult = "FAIL: couldnt sent notification : " + ex.getMessage();
            _logger.info(strCallResult);
            resp.getWriter().println(strCallResult);
        }
    }


    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
