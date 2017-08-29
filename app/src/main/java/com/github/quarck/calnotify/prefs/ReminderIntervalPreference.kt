//
//   Calendar Notifications Plus
//   Copyright (C) 2016 Sergey Parshin (s.parshin.sc@gmail.com)
//
//   This program is free software; you can redistribute it and/or modify
//   it under the terms of the GNU General Public License as published by
//   the Free Software Foundation; either version 3 of the License, or
//   (at your option) any later version.
//
//   This program is distributed in the hope that it will be useful,
//   but WITHOUT ANY WARRANTY; without even the implied warranty of
//   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//   GNU General Public License for more details.
//
//   You should have received a copy of the GNU General Public License
//   along with this program; if not, write to the Free Software Foundation,
//   Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
//

package com.github.quarck.calnotify.prefs

import android.app.AlertDialog
import android.content.Context
import android.content.res.TypedArray
import android.preference.DialogPreference
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import com.github.quarck.calnotify.Consts
import com.github.quarck.calnotify.R
import com.github.quarck.calnotify.Settings
//import com.github.quarck.calnotify.logs.Logger
import com.github.quarck.calnotify.ui.TimeIntervalPickerController
import com.github.quarck.calnotify.utils.isMarshmallowOrAbove

class ReminderIntervalPreference(context: Context, attrs: AttributeSet) : DialogPreference(context, attrs) {
    internal var timeValueSeconds = 0

    internal lateinit var picker: TimeIntervalPickerController

    init {
        dialogLayoutResource = R.layout.dialog_interval_picker
        setPositiveButtonText(android.R.string.ok)
        setNegativeButtonText(android.R.string.cancel)
        dialogIcon = null
    }

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)

        val enableSubMinute = Settings(this.context).enableSubMinuteReminders

        picker = TimeIntervalPickerController(view, R.string.empty, enableSubMinute)
        picker.intervalSeconds = timeValueSeconds
    }

    override fun onClick() {
        super.onClick()
        picker.clearFocus()
    }

    override fun onDialogClosed(positiveResult: Boolean) {

        if (positiveResult) {
            picker.clearFocus()

            timeValueSeconds = picker.intervalSeconds

            if (timeValueSeconds == 0) {
                timeValueSeconds = 60
                val msg = context.resources.getString(R.string.invalid_reminder_interval)
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }

            persistInt(timeValueSeconds)

            val settings = Settings(context)

            if (isMarshmallowOrAbove &&
                    timeValueSeconds * 1000L < Consts.MARSHMALLOW_MIN_REMINDER_INTERVAL_USEC &&
                    !settings.dontShowMarshmallowWarningInSettings) {

                AlertDialog.Builder(context)
                        .setMessage(context.resources.getString(R.string.reminders_not_accurate_again))
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok) {
                            _, _ ->
                        }
                        .setNegativeButton(R.string.never_show_again) {
                            _, _ ->
                            Settings(context).dontShowMarshmallowWarningInSettings = true
                        }
                        .create()
                        .show()
            }
        }
    }

    override fun onSetInitialValue(restorePersistedValue: Boolean, defaultValue: Any?) {
        if (restorePersistedValue) {
            // Restore existing state
            timeValueSeconds = this.getPersistedInt(0)
        }
        else if (defaultValue != null && defaultValue is Int) {
            // Set default state from the XML attribute
            timeValueSeconds = defaultValue
            persistInt(timeValueSeconds)
        }
    }

    @Suppress("UseExpressionBody")
    override fun onGetDefaultValue(a: TypedArray, index: Int): Any {
        return a.getInteger(index, 600)
    }

    companion object {
        private const val LOG_TAG = "TimePickerPreference"
    }
}