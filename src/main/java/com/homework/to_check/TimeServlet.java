package com.homework.to_check;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@WebServlet(urlPatterns = "/time", name = "TimeServlet")
public class TimeServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(TimeServlet.class);
    public static final int TIME_WITHOUT_MILLIS_LENGTH = 19;
    private static final String RESPONSE_WRITER_ERROR_MSG = "Response writer error!";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        String timeZone = Optional.ofNullable(req.getParameter("timezone")).orElse("UTC+0");
        String parsedTimeZone = timeZone.replace("+","");
        int timeZoneInt = Integer.parseInt(parsedTimeZone.substring(3).trim());
        String time = LocalDateTime.now(ZoneOffset.ofHours(timeZoneInt)).toString();
        time = time.replace("T", " ").substring(0, TIME_WITHOUT_MILLIS_LENGTH) + " " + timeZone;
        try(PrintWriter writer = resp.getWriter()) {
            writer.write(time);
            writer.flush();
        } catch (IOException e) {
            logger.error(RESPONSE_WRITER_ERROR_MSG);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
