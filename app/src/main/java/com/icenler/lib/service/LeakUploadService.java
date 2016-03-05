package com.icenler.lib.service;

import com.squareup.leakcanary.AnalysisResult;
import com.squareup.leakcanary.DisplayLeakService;
import com.squareup.leakcanary.HeapDump;

/**
 * Created by iCenler on 2016/3/5.
 * Description:
 */
public class LeakUploadService extends DisplayLeakService {

    @Override
    protected void afterDefaultHandling(HeapDump heapDump, AnalysisResult result, String leakInfo) {
        // TODO LeakCanary 日志上传
        // mServer.uploadLeakBlocking(heapDump.heapDumpFile, leakInfo);
    }
}
