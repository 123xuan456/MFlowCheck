package com.mtm.flowcheck.utils.recog;

/**
 * Created by fujiayi on 2017/6/14.
 */

public interface IStatus {

    int STATUS_NONE = 2;

    int STATUS_READY = 3;
    int STATUS_SPEAKING = 4;
    int STATUS_RECOGNITION = 5;

    int STATUS_FINISHED = 6;
    int STATUS_LONG_SPEECH_FINISHED = 7;
    int STATUS_STOPPED = 10;

    int STATUS_START = 11;
    int STATUS_RESUME = 12;
    int STATUS_PAUSE = 13;
    int STATUS_STOP = 14;
    int STATUS_FINSIH = 15;

    int STATUS_WAITING_READY = 8001;
    int WHAT_MESSAGE_STATUS = 9001;

    int STATUS_WAKEUP_SUCCESS = 7001;
    int STATUS_WAKEUP_EXIT = 7003;
}
