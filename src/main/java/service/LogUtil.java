package service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class LogUtil {


private static final Logger log= LogManager.getLogger("console");

    public static Logger getLogger() {
        return log;
    }
}
