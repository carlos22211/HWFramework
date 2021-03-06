package com.android.server.content;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;

public class SyncJobService extends JobService {
    public static final String EXTRA_MESSENGER = "messenger";
    private static final String TAG = "SyncManager";
    private SparseArray<JobParameters> jobParamsMap;
    private Messenger mMessenger;

    public SyncJobService() {
        this.jobParamsMap = new SparseArray();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        this.mMessenger = (Messenger) intent.getParcelableExtra(EXTRA_MESSENGER);
        Message m = Message.obtain();
        m.what = 7;
        m.obj = this;
        sendMessage(m);
        return 2;
    }

    private void sendMessage(Message message) {
        if (this.mMessenger == null) {
            Slog.e(TAG, "Messenger not initialized.");
            return;
        }
        try {
            this.mMessenger.send(message);
        } catch (RemoteException e) {
            Slog.e(TAG, e.toString());
        }
    }

    public boolean onStartJob(JobParameters params) {
        boolean isLoggable = Log.isLoggable(TAG, 2);
        synchronized (this.jobParamsMap) {
            Slog.i(TAG, "SyncJobService->onStartJob:put job to map, jobid:" + params.getJobId());
            this.jobParamsMap.put(params.getJobId(), params);
        }
        Message m = Message.obtain();
        m.what = 10;
        SyncOperation op = SyncOperation.maybeCreateFromJobExtras(params.getExtras());
        if (op == null) {
            Slog.e(TAG, "Got invalid job " + params.getJobId());
            return false;
        }
        if (isLoggable) {
            Slog.v(TAG, "Got start job message " + op.target);
        }
        m.obj = op;
        sendMessage(m);
        return true;
    }

    public boolean onStopJob(JobParameters params) {
        int i = 1;
        if (Log.isLoggable(TAG, 2)) {
            Slog.v(TAG, "onStopJob called " + params.getJobId() + ", reason: " + params.getStopReason());
        }
        synchronized (this.jobParamsMap) {
            Slog.i(TAG, "SyncJobService->onStopJob: remove job to map, jobid:" + params.getJobId());
            this.jobParamsMap.remove(params.getJobId());
        }
        Message m = Message.obtain();
        m.what = 11;
        m.obj = SyncOperation.maybeCreateFromJobExtras(params.getExtras());
        if (m.obj == null) {
            return false;
        }
        m.arg1 = params.getStopReason() != 0 ? 1 : 0;
        if (params.getStopReason() != 3) {
            i = 0;
        }
        m.arg2 = i;
        sendMessage(m);
        return false;
    }

    public void callJobFinished(int jobId, boolean needsReschedule) {
        synchronized (this.jobParamsMap) {
            JobParameters params = (JobParameters) this.jobParamsMap.get(jobId);
            if (params != null) {
                Slog.i(TAG, "SyncJobService->callJobFinished: remove job to map, jobid:" + jobId);
                jobFinished(params, needsReschedule);
                this.jobParamsMap.remove(jobId);
            } else {
                Slog.e(TAG, "Job params not found for " + String.valueOf(jobId));
            }
        }
    }
}
