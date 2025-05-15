package com.violin.demo.matrix

import android.content.Context
import com.tencent.matrix.plugin.DefaultPluginListener
import com.tencent.matrix.report.Issue
import com.tencent.matrix.util.MatrixLog

class MatrixPluginListener(context: Context) : DefaultPluginListener(context) {
    override fun onReportIssue(issue: Issue?) {
        super.onReportIssue(issue)
        MatrixLog.e("MatrixPluginListener", issue.toString());
    }
}